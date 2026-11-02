package com.example.marketcoinslist.watch_list.domain.repository

import com.example.marketcoinslist.coin_list.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface WatchListRepository {
    fun getWatchList(): Flow<List<Coin>>
    suspend fun removeFromWatchList(coinId: String)
}