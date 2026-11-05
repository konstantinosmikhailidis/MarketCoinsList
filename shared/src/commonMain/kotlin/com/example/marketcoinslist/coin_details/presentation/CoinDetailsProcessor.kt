package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.coin_details.domain.repository.CoinDetailsRepository
import com.example.marketcoinslist.core.notification.NotificationInfo
import com.example.marketcoinslist.core.viewmodel.Processor

class CoinDetailsProcessor(
    private val repository: CoinDetailsRepository
) : Processor<CoinDetailsAction, CoinDetailsSideEffect> {

    override suspend fun process(effect: CoinDetailsSideEffect): CoinDetailsAction {
        return when (effect) {
            is CoinDetailsSideEffect.FetchMarketChart -> fetchMarketChart(effect)
            is CoinDetailsSideEffect.ToggleFavorite -> toggleFavorite(effect)
        }
    }

    override fun onError(message: String) = CoinDetailsAction.LoadNotification(
        notificationInfo = NotificationInfo(
            message = message
        )
    )


    private suspend fun fetchMarketChart(effect: CoinDetailsSideEffect.FetchMarketChart)  = safeAction(defaultErrorMessage = "Failed to load chart") {
        val marketChart = repository.getMarketChart(effect.coinId, effect.days)
        CoinDetailsAction.MarketChartUpdated(marketChart)
    }

    private suspend fun toggleFavorite(effect: CoinDetailsSideEffect.ToggleFavorite) = safeAction {
        repository.toggleFavorite(effect.coinId)
        CoinDetailsAction.DetailsLoaded
    }
}