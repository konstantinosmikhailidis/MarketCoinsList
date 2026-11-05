package com.example.marketcoinslist.coins.data.remote

import com.example.marketcoinslist.coins.data.remote.dto.CoinDto
import com.example.marketcoinslist.coins.data.remote.dto.CoinGeckoErrorDto
import com.example.marketcoinslist.coins.data.remote.dto.MarketChartDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class CoinGeckoApi(private val httpClient: HttpClient) {

    suspend fun getCoinsMarkets(
        currency: String = "usd",
        order: String = "market_cap_desc",
        perPage: Int = 100,
        page: Int = 1,
        sparkline: Boolean = false
    ): List<CoinDto> {
        val response = httpClient.get("https://api.coingecko.com/api/v3/coins/markets") {
            parameter("vs_currency", currency)
            parameter("order", order)
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("sparkline", sparkline)
        }
        return response.bodyOrThrow()
    }

    suspend fun getMarketChart(
        coinId: String,
        currency: String = "usd",
        days: String = "1"
    ): MarketChartDto {
        val response = httpClient.get("https://api.coingecko.com/api/v3/coins/$coinId/market_chart") {
            parameter("vs_currency", currency)
            parameter("days", days)
        }
        return response.bodyOrThrow()
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
        if (status == HttpStatusCode.OK) {
            return body<T>()
        } else {
            val errorDto = runCatching { body<CoinGeckoErrorDto>() }.getOrNull()
            val errorMessage = errorDto?.status?.errorMessage
                ?: errorDto?.error
                ?: "HTTP ${status.value}: ${status.description}"
            throw Exception(errorMessage)
        }
    }
}