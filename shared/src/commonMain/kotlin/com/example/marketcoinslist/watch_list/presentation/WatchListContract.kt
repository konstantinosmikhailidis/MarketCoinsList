package com.example.marketcoinslist.watch_list.presentation

import com.example.marketcoinslist.coin_list.domain.model.Coin

data class WatchListState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface WatchListAction {
    data class WatchListUpdated(val coins: List<Coin>) : WatchListAction
    data class RemoveFavorite(val id: String) : WatchListAction
    data object Nothing : WatchListAction
}

sealed interface WatchListSideEffect {
    data class RemoveFavorite(val id: String) : WatchListSideEffect
}

sealed interface WatchListEvent
