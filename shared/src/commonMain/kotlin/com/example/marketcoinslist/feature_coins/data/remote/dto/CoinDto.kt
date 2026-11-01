package com.example.marketcoinslist.feature_coins.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerialName("current_price")
    val currentPrice: Double,
    @SerialName("market_cap")
    val marketCap: Double? = null,
    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null,
    @SerialName("total_volume")
    val totalVolume: Double? = null,
    @SerialName("high_24h")
    val high24h: Double? = null,
    @SerialName("low_24h")
    val low24h: Double? = null,
    @SerialName("price_change_24h")
    val priceChange24h: Double? = null,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double? = null
)