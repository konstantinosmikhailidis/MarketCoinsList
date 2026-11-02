package com.example.marketcoinslist.coin_list.domain.repository

import com.example.marketcoinslist.coin_list.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<List<Coin>>
    suspend fun refreshCoins(force: Boolean = false): Result<Unit>
    fun getFavoriteCoins(): Flow<List<Coin>>
    suspend fun toggleFavorite(coinId: String)
}