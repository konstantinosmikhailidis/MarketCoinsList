package com.example.marketcoinslist.coins.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.marketcoinslist.database.MarketCoinsDatabase
import com.example.marketcoinslist.coins.data.mapper.toDomain
import com.example.marketcoinslist.coins.data.mapper.toEntity
import com.example.marketcoinslist.coins.data.remote.CoinGeckoApi
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.coins.domain.repository.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CoinRepositoryImpl(
    private val api: CoinGeckoApi,
    private val db: MarketCoinsDatabase
) : CoinRepository {

    override fun getCoins(): Flow<List<Coin>> {
        val coinsFlow = db.marketCoinsDatabaseQueries.selectAllCoins().asFlow().mapToList(Dispatchers.IO)
        val favoritesFlow = db.marketCoinsDatabaseQueries.selectAllFavorites().asFlow().mapToList(Dispatchers.IO)

        return combine(coinsFlow, favoritesFlow) { coins, favorites ->
            val favoriteIds = favorites.toSet()
            coins.map { it.toDomain(isFavorite = favoriteIds.contains(it.id)) }
        }
    }

    override suspend fun refreshCoins(force: Boolean): Result<Unit> {
        return try {
            val remoteCoins = api.getCoinsMarkets()
            withContext(Dispatchers.IO) {
                db.marketCoinsDatabaseQueries.transaction {
                    remoteCoins.forEach { dto ->
                        val entity = dto.toEntity()
                        db.marketCoinsDatabaseQueries.insertCoin(
                            id = entity.id,
                            symbol = entity.symbol,
                            name = entity.name,
                            image = entity.image,
                            currentPrice = entity.currentPrice,
                            priceChange24h = entity.priceChange24h,
                            marketCapRank = entity.marketCapRank
                        )
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override fun getFavoriteCoins(): Flow<List<Coin>> {
        return getCoins().map { coins -> coins.filter { it.isFavorite } }
    }

    override suspend fun toggleFavorite(coinId: String) {
        withContext(Dispatchers.IO) {
            val isFavorite = db.marketCoinsDatabaseQueries.isFavorite(coinId).executeAsOne() > 0
            if (isFavorite) {
                db.marketCoinsDatabaseQueries.deleteFavorite(coinId)
            } else {
                db.marketCoinsDatabaseQueries.insertFavorite(coinId)
            }
        }
    }
}