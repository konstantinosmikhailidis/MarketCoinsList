package com.example.marketcoinslist.feature_coins.presentation

import com.example.marketcoinslist.core.Processor
import com.example.marketcoinslist.feature_coins.domain.repository.CoinRepository

class CoinsProcessor(
    private val repository: CoinRepository
) : Processor<CoinsAction, CoinsSideEffect> {

    override suspend fun process(effect: CoinsSideEffect): CoinsAction {
        return when (effect) {
            is CoinsSideEffect.LoadCoins -> safeAction(defaultErrorMessage = "Failed to load coins") {
                repository.refreshCoins()
                CoinsAction.CoinsRefreshed
            }
            is CoinsSideEffect.ToggleFavorite -> safeAction {
                repository.toggleFavorite(effect.id)
                CoinsAction.CoinsRefreshed // We just return a success action, flow updates UI
            }
        }
    }

    override fun onError(message: String): CoinsAction {
        return CoinsAction.RefreshFailed(message)
    }
}