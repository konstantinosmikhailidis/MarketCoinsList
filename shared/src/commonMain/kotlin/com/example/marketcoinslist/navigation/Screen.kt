package com.example.marketcoinslist.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey

@Serializable
data object Coins : Screen

@Serializable
data class CoinDetails(val id: String) : Screen

@Serializable
data object WatchList : Screen