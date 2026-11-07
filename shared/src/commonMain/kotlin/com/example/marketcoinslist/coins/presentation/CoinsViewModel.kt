package com.example.marketcoinslist.coins.presentation

import androidx.lifecycle.viewModelScope
import com.example.marketcoinslist.core.viewmodel.BaseViewModel
import com.example.marketcoinslist.core.viewmodel.DispatchersProvider
import com.example.marketcoinslist.core.viewmodel.Mapper
import com.example.marketcoinslist.coins.domain.repository.CoinRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CoinsStateMapper : Mapper<CoinsState, CoinsState> {
    override fun map(s: CoinsState, vararg args: Any?): CoinsState = s
}

class CoinsViewModel(
    private val repository: CoinRepository,
    dispatchers: DispatchersProvider,
    updater: CoinsUpdater,
    processor: CoinsProcessor
) : BaseViewModel<CoinsState, CoinsAction, CoinsSideEffect, CoinsEvent, CoinsState>(
    initialState = CoinsState(),
    dispatchers = dispatchers,
    initialEffects = setOf(CoinsSideEffect.LoadCoins),
    updater = updater,
    processor = processor,
    stateUiMapper = CoinsStateMapper()
) {

    init {
        repository.getCoins()
            .onEach { coins ->
                handle(CoinsAction.CoinsUpdated(coins))
            }
            .launchIn(viewModelScope)
    }
}