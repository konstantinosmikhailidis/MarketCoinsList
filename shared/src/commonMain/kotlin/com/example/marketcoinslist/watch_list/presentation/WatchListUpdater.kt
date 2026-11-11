package com.example.marketcoinslist.watch_list.presentation

import com.example.marketcoinslist.core.viewmodel.StateHolder
import com.example.marketcoinslist.core.viewmodel.StateUpdater

class WatchListUpdater : StateUpdater<WatchListState, WatchListAction, WatchListSideEffect, WatchListEvent> {

    override fun update(
        action: WatchListAction,
        currentState: WatchListState
    ): StateHolder<WatchListState, WatchListSideEffect, WatchListEvent> {
        return when (action) {
            is WatchListAction.WatchListUpdated -> {
                StateHolder(currentState.copy(coins = action.coins))
            }
            is WatchListAction.RemoveFavorite -> {
                StateHolder(
                    state = currentState,
                    effects = setOf(WatchListSideEffect.RemoveFavorite(action.id))
                )
            }
            is WatchListAction.SetFilter -> {
                StateHolder(currentState.copy(filterType = action.filterType))
            }
            is WatchListAction.Nothing -> {
                StateHolder(currentState)
            }
        }
    }
}