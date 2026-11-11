package com.example.marketcoinslist.coins.presentation

import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo

data class CoinsState(
    val coins: List<Coin> = emptyList(),
    val filterType: FilterType = FilterType.ALL
)

enum class FilterType {
    ALL, TOP_50, GAINERS, LOSERS
}

sealed interface CoinsAction {
    data class CoinsUpdated(val coins: List<Coin>) : CoinsAction
    data object RefreshCoins : CoinsAction
    data object CoinsRefreshed : CoinsAction
    data class RefreshFailed(val notificationInfo: NotificationInfo) : CoinsAction
    data class ToggleFavorite(val id: String) : CoinsAction
    data class SetFilter(val filterType: FilterType) : CoinsAction
}

sealed interface CoinsSideEffect {
    data object LoadCoins : CoinsSideEffect
    data class ToggleFavorite(val id: String) : CoinsSideEffect
}

sealed interface CoinsEvent {
    data class ShowNotification(val notificationInfo: NotificationInfo) : CoinsEvent
    data class ShowLoadingAnimation(val loadingInfo: LoadingInfo?): CoinsEvent
}