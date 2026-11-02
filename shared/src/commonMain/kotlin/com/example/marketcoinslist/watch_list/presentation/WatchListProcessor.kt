package com.example.marketcoinslist.watch_list.presentation

import com.example.marketcoinslist.core.viewmodel.Processor
import com.example.marketcoinslist.watch_list.domain.repository.WatchListRepository

class WatchListProcessor(
    private val repository: WatchListRepository
) : Processor<WatchListAction, WatchListSideEffect> {

    override suspend fun process(effect: WatchListSideEffect): WatchListAction {
        return when (effect) {
            is WatchListSideEffect.RemoveFavorite -> safeAction {
                repository.removeFromWatchList(effect.id)
                WatchListAction.Nothing // The UI updates via the flow from DB
            }
        }
    }

    override fun onError(message: String): WatchListAction {
        return WatchListAction.Nothing
    }
}