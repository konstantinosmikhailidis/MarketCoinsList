package com.example.marketcoinslist.coin_list.domain.model

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double?,
    val marketCapRank: Int?,
    val isFavorite: Boolean = false
)