package com.example.marketcoinslist.coin_details.presentation

import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.core.viewmodel.StateHolder
import com.example.marketcoinslist.core.viewmodel.StateUpdater

class CoinDetailsUpdater :
    StateUpdater<CoinDetailsState, CoinDetailsAction, CoinDetailsSideEffect, CoinDetailsEvent> {

    override fun update(
        action: CoinDetailsAction,
        currentState: CoinDetailsState,
    ): StateHolder<CoinDetailsState, CoinDetailsSideEffect, CoinDetailsEvent> {
        return when (action) {
            is CoinDetailsAction.CoinUpdated -> {
                StateHolder(currentState.copy(coin = action.coin))
            }

            is CoinDetailsAction.MarketChartUpdated -> {
                StateHolder(
                    state = currentState.copy(
                        marketChart = action.marketChart,
                    ),
                    events = setOf(
                        CoinDetailsEvent.ShowLoadingAnimation(null)
                    )
                )
            }

            is CoinDetailsAction.LoadDetails -> {
                StateHolder(
                    state = currentState,
                    effects = setOf(
                        CoinDetailsSideEffect.FetchMarketChart(
                            action.coinId,
                            currentState.selectedDays
                        )
                    ),
                    events = setOf(
                        CoinDetailsEvent.ShowLoadingAnimation(LoadingInfo())
                    )
                )
            }

            is CoinDetailsAction.SelectTimeframe -> {
                StateHolder(
                    state = currentState.copy(
                        selectedDays = action.days,
                    ),
                    effects = setOf(
                        CoinDetailsSideEffect.FetchMarketChart(
                            action.coinId,
                            action.days
                        )
                    ),
                    events = setOf(
                        CoinDetailsEvent.ShowLoadingAnimation(LoadingInfo())
                    )
                )
            }

            is CoinDetailsAction.DetailsLoaded -> {
                StateHolder(
                    state = currentState,
                    events = setOf(
                        CoinDetailsEvent.ShowLoadingAnimation(null)
                    )
                )
            }

            is CoinDetailsAction.LoadNotification -> {
                StateHolder(
                    state = currentState,
                    events = setOf(
                        CoinDetailsEvent.ShowLoadingAnimation(null),
                        CoinDetailsEvent.ShowNotification(action.notificationInfo)
                    )
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