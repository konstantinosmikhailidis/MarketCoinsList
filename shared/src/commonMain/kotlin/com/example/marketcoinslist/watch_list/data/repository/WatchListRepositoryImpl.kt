package com.example.marketcoinslist.watch_list.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.marketcoinslist.coins.data.mapper.toDomain
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.database.MarketCoinsDatabase
import com.example.marketcoinslist.watch_list.domain.repository.WatchListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WatchListRepositoryImpl(
    private val db: MarketCoinsDatabase
) : WatchListRepository {

    override fun getWatchList(): Flow<List<Coin>> {
        return db.marketCoinsDatabaseQueries.selectFavoriteCoinsInfo()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { it.toDomain(isFavorite = true) }
            }
    }

    override suspend fun removeFromWatchList(coinId: String) {
        withContext(Dispatchers.IO) {
            db.marketCoinsDatabaseQueries.deleteFavorite(coinId)
        }
    }
}