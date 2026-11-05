package com.example.marketcoinslist.coins.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarketChartDto(
    val prices: List<List<Double>> = emptyList(),
    val market_caps: List<List<Double>> = emptyList(),
    val total_volumes: List<List<Double>> = emptyList()
)