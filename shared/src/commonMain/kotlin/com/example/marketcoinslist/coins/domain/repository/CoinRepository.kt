package com.example.marketcoinslist.coins.domain.repository

import com.example.marketcoinslist.coins.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<List<Coin>>
    suspend fun refreshCoins(force: Boolean = false): Result<Unit>
    fun getFavoriteCoins(): Flow<List<Coin>>
    suspend fun toggleFavorite(coinId: String)
}