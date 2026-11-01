package com.example.marketcoinslist.feature_coins.data.remote

import com.example.marketcoinslist.feature_coins.data.remote.dto.CoinDto
import com.example.marketcoinslist.feature_coins.data.remote.dto.MarketChartDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CoinGeckoApi(private val httpClient: HttpClient) {

    suspend fun getCoinsMarkets(
        currency: String = "usd",
        order: String = "market_cap_desc",
        perPage: Int = 100,
        page: Int = 1,
        sparkline: Boolean = false
    ): List<CoinDto> {
        return httpClient.get("https://api.coingecko.com/api/v3/coins/markets") {
            parameter("vs_currency", currency)
            parameter("order", order)
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("sparkline", sparkline)
        }.body()
    }

    suspend fun getMarketChart(
        coinId: String,
        currency: String = "usd",
        days: String = "1"
    ): MarketChartDto {
        return httpClient.get("https://api.coingecko.com/api/v3/coins/$coinId/market_chart") {
            parameter("vs_currency", currency)
            parameter("days", days)
        }.body()
    }
}