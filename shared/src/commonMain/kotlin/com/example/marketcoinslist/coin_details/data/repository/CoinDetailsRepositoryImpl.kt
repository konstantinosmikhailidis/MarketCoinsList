package com.example.marketcoinslist.coin_details.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.marketcoinslist.coin_details.domain.model.MarketChart
import com.example.marketcoinslist.coin_details.domain.model.PricePoint
import com.example.marketcoinslist.coin_details.domain.repository.CoinDetailsRepository
import com.example.marketcoinslist.coins.data.mapper.toDomain
import com.example.marketcoinslist.coins.data.remote.CoinGeckoApi
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.database.MarketCoinsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

/**
 * Implementation of [CoinDetailsRepository] that acts as a Single Source of Truth (SSOT).
 * 
 * It combines data from the local SQLDelight database (favorites state, cached coin data)
 * with remote data (Market Charts) fetched via Ktor.
 */
class CoinDetailsRepositoryImpl(
    private val api: CoinGeckoApi,
    private val db: MarketCoinsDatabase
) : CoinDetailsRepository {

    override fun getCoinDetails(id: String): Flow<Coin?> {
        val coinFlow = db.marketCoinsDatabaseQueries.selectCoinById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
        val favoritesFlow = db.marketCoinsDatabaseQueries.isFavorite(id).asFlow().mapToOneOrNull(Dispatchers.IO)

        return combine(coinFlow, favoritesFlow) { coinEntity, isFavoriteCount ->
            coinEntity?.toDomain(isFavorite = (isFavoriteCount ?: 0) > 0)
        }
    }

    override suspend fun getMarketChart(coinId: String, days: String): MarketChart {
            val dto = api.getMarketChart(coinId = coinId, days = days)
            println("MarketChartDto fetched for $coinId with ${dto.prices.size} prices")
            val prices = dto.prices.mapNotNull { point ->
                if (point.size >= 2) {
                    PricePoint(
                        timestamp = point[0].toLong(),
                        price = point[1]
                    )
                } else null
            }
            return MarketChart(prices = prices)
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