package com.example.marketcoinslist.coins.presentation

import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.viewmodel.StateHolder
import com.example.marketcoinslist.core.viewmodel.StateUpdater

class CoinsUpdater : StateUpdater<CoinsState, CoinsAction, CoinsSideEffect, CoinsEvent> {

    override fun update(
        action: CoinsAction,
        currentState: CoinsState
    ): StateHolder<CoinsState, CoinsSideEffect, CoinsEvent> {
        return when (action) {
            is CoinsAction.CoinsUpdated -> {
                StateHolder(currentState.copy(coins = action.coins))
            }
            is CoinsAction.RefreshCoins -> {
                StateHolder(
                    state = currentState,
                    effects = setOf(CoinsSideEffect.LoadCoins),
                    events = setOf(CoinsEvent.ShowLoadingAnimation(LoadingInfo()))
                )
            }
            is CoinsAction.CoinsRefreshed -> {
                StateHolder(
                    state = currentState,
                    events = setOf(
                        CoinsEvent.ShowLoadingAnimation(null)
                    )
                )
            }
            is CoinsAction.RefreshFailed -> {
                StateHolder(
                    state = currentState,
                    events = setOf(
                        CoinsEvent.ShowNotification(action.notificationInfo),
                        CoinsEvent.ShowLoadingAnimation(null)
                    )
                )
            }
            is CoinsAction.ToggleFavorite -> {
                StateHolder(
                    state = currentState,
                    effects = setOf(CoinsSideEffect.ToggleFavorite(action.id))
                )
            }
            is CoinsAction.SetFilter -> {
                StateHolder(currentState.copy(filterType = action.filterType))
            }
        }
    }
}