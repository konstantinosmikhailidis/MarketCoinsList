package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.coin_details.domain.model.MarketChart
import com.example.marketcoinslist.coin_list.domain.model.Coin

data class CoinDetailsState(
    val coin: Coin? = null,
    val marketChart: MarketChart? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface CoinDetailsAction {
    data class CoinUpdated(val coin: Coin?) : CoinDetailsAction
    data class MarketChartUpdated(val marketChart: MarketChart) : CoinDetailsAction
    data class LoadDetails(val coinId: String) : CoinDetailsAction
    data object DetailsLoaded : CoinDetailsAction
    data class LoadFailed(val error: String) : CoinDetailsAction
    data class ToggleFavorite(val coinId: String) : CoinDetailsAction
}

sealed interface CoinDetailsSideEffect {
    data class FetchMarketChart(val coinId: String, val days: String = "1") : CoinDetailsSideEffect
    data class ToggleFavorite(val coinId: String) : CoinDetailsSideEffect
}

sealed interface CoinDetailsEvent {
    data class ShowError(val message: String) : CoinDetailsEvent
}