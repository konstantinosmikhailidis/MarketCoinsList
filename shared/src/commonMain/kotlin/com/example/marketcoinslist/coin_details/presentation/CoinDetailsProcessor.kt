package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.coin_details.domain.repository.CoinDetailsRepository
import com.example.marketcoinslist.core.viewmodel.Processor

class CoinDetailsProcessor(
    private val repository: CoinDetailsRepository
) : Processor<CoinDetailsAction, CoinDetailsSideEffect> {

    override suspend fun process(effect: CoinDetailsSideEffect): CoinDetailsAction {
        return when (effect) {
            is CoinDetailsSideEffect.FetchMarketChart -> safeAction(defaultErrorMessage = "Failed to load chart") {
                val result = repository.getMarketChart(effect.coinId, effect.days)
                if (result.isSuccess) {
                    CoinDetailsAction.MarketChartUpdated(result.getOrThrow())
                } else {
                    CoinDetailsAction.LoadFailed(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
            is CoinDetailsSideEffect.ToggleFavorite -> safeAction {
                repository.toggleFavorite(effect.coinId)
                CoinDetailsAction.DetailsLoaded // UI updates via Flow
            }
        }
    }

    override fun onError(message: String): CoinDetailsAction {
        return CoinDetailsAction.LoadFailed(message)
    }
}