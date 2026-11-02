package com.example.marketcoinslist.coin_list.presentation

import com.example.marketcoinslist.core.viewmodel.StateHolder
import com.example.marketcoinslist.core.viewmodel.StateUpdater

class CoinsUpdater : StateUpdater<CoinsState, CoinsAction, CoinsSideEffect, CoinsEvent> {

    override fun update(
        action: CoinsAction,
        currentState: CoinsState
    ): StateHolder<CoinsState, CoinsSideEffect, CoinsEvent> {
        return when (action) {
            is CoinsAction.CoinsUpdated -> {
                StateHolder(currentState.copy(coins = action.coins, error = null))
            }
            is CoinsAction.RefreshCoins -> {
                StateHolder(
                    state = currentState.copy(isLoading = true, error = null),
                    effects = setOf(CoinsSideEffect.LoadCoins)
                )
            }
            is CoinsAction.CoinsRefreshed -> {
                StateHolder(currentState.copy(isLoading = false))
            }
            is CoinsAction.RefreshFailed -> {
                StateHolder(
                    state = currentState.copy(isLoading = false, error = action.error),
                    events = setOf(CoinsEvent.ShowError(action.error))
                )
            }
            is CoinsAction.ToggleFavorite -> {
                StateHolder(
                    state = currentState,
                    effects = setOf(CoinsSideEffect.ToggleFavorite(action.id))
                )
            }
        }
    }
}