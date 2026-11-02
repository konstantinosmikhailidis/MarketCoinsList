package com.example.marketcoinslist.watch_list.presentation

import androidx.lifecycle.viewModelScope
import com.example.marketcoinslist.core.viewmodel.BaseViewModel
import com.example.marketcoinslist.core.viewmodel.DispatchersProvider
import com.example.marketcoinslist.core.viewmodel.Mapper
import com.example.marketcoinslist.watch_list.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WatchListStateMapper : Mapper<WatchListState, WatchListState> {
    override fun map(s: WatchListState, vararg args: Any?): WatchListState = s
}

class WatchListViewModel(
    private val repository: WatchListRepository,
    dispatchers: DispatchersProvider,
    updater: WatchListUpdater,
    processor: WatchListProcessor
) : BaseViewModel<WatchListState, WatchListAction, WatchListSideEffect, WatchListEvent, WatchListState>(
    initialState = WatchListState(isLoading = true),
    dispatchers = dispatchers,
    updater = updater,
    processor = processor,
    stateUiMapper = WatchListStateMapper()
) {
    init {
        repository.getWatchList()
            .onEach { coins ->
                handle(WatchListAction.WatchListUpdated(coins))
            }
            .launchIn(viewModelScope)
    }
}