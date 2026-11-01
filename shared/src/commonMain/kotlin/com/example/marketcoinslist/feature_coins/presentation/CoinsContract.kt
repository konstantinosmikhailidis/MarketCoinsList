package com.example.marketcoinslist.feature_coins.presentation

import com.example.marketcoinslist.feature_coins.domain.model.Coin

data class CoinsState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface CoinsAction {
    data class CoinsUpdated(val coins: List<Coin>) : CoinsAction
    data object RefreshCoins : CoinsAction
    data object CoinsRefreshed : CoinsAction
    data class RefreshFailed(val error: String) : CoinsAction
    data class ToggleFavorite(val id: String) : CoinsAction
}

sealed interface CoinsSideEffect {
    data object LoadCoins : CoinsSideEffect
    data class ToggleFavorite(val id: String) : CoinsSideEffect
}

sealed interface CoinsEvent {
    data class ShowError(val message: String) : CoinsEvent
}
