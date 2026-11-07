package com.example.marketcoinslist.coins.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinGeckoErrorDto(
    val status: CoinGeckoStatusDto? = null,
    val error: String? = null
)

@Serializable
data class CoinGeckoStatusDto(
    @SerialName("error_code") val errorCode: Int? = null,
    @SerialName("error_message") val errorMessage: String? = null
)