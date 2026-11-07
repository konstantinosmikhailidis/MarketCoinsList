package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.coin_details.domain.model.MarketChart
import com.example.marketcoinslist.coins.domain.model.Coin
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.notification.NotificationInfo

data class CoinDetailsState(
    val coin: Coin? = null,
    val marketChart: MarketChart? = null,
    val selectedDays: String = "1"
)

sealed interface CoinDetailsAction {
    data class CoinUpdated(val coin: Coin?) : CoinDetailsAction
    data class MarketChartUpdated(val marketChart: MarketChart) : CoinDetailsAction
    data class LoadDetails(val coinId: String) : CoinDetailsAction
    data class SelectTimeframe(val coinId: String, val days: String) : CoinDetailsAction
    data object DetailsLoaded : CoinDetailsAction
    data class LoadNotification(val notificationInfo: NotificationInfo) : CoinDetailsAction
    data class ToggleFavorite(val coinId: String) : CoinDetailsAction
}

sealed interface CoinDetailsSideEffect {
    data class FetchMarketChart(val coinId: String, val days: String = "1") : CoinDetailsSideEffect
    data class ToggleFavorite(val coinId: String) : CoinDetailsSideEffect
}

sealed interface CoinDetailsEvent {
    data class ShowNotification(val notificationInfo: NotificationInfo) : CoinDetailsEvent

    data class ShowLoadingAnimation(val loadingInfo: LoadingInfo?): CoinDetailsEvent
}