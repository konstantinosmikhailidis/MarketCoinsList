package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.core.viewmodel.StateHolder
import com.example.marketcoinslist.core.viewmodel.StateUpdater

class CoinDetailsUpdater : StateUpdater<CoinDetailsState, CoinDetailsAction, CoinDetailsSideEffect, CoinDetailsEvent> {

    override fun update(
        action: CoinDetailsAction,
        currentState: CoinDetailsState
    ): StateHolder<CoinDetailsState, CoinDetailsSideEffect, CoinDetailsEvent> {
        return when (action) {
            is CoinDetailsAction.CoinUpdated -> {
                StateHolder(currentState.copy(coin = action.coin, error = null))
            }
            is CoinDetailsAction.MarketChartUpdated -> {
                StateHolder(currentState.copy(marketChart = action.marketChart, isLoading = false, error = null))
            }
            is CoinDetailsAction.LoadDetails -> {
                StateHolder(
                    state = currentState.copy(isLoading = true, error = null),
                    effects = setOf(CoinDetailsSideEffect.FetchMarketChart(action.coinId))
                )
            }
            is CoinDetailsAction.DetailsLoaded -> {
                StateHolder(currentState.copy(isLoading = false))
            }
            is CoinDetailsAction.LoadFailed -> {
                StateHolder(
                    state = currentState.copy(isLoading = false, error = action.error),
                    events = setOf(CoinDetailsEvent.ShowError(action.error))
                )
            }
            is CoinDetailsAction.ToggleFavorite -> {
                StateHolder(
                    state = currentState,
                    effects = setOf(CoinDetailsSideEffect.ToggleFavorite(action.coinId))
                )
            }
        }
    }
}