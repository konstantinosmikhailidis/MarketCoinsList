package com.example.marketcoinslist.coin_details.domain.repository

import com.example.marketcoinslist.coin_details.domain.model.MarketChart
import com.example.marketcoinslist.coins.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinDetailsRepository {
    fun getCoinDetails(id: String): Flow<Coin?>
    suspend fun getMarketChart(coinId: String, days: String = "1"): MarketChart
    suspend fun toggleFavorite(coinId: String)
}