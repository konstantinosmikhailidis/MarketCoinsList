package com.example.marketcoinslist.coin_details.presentation

import androidx.lifecycle.viewModelScope
import com.example.marketcoinslist.coin_details.domain.repository.CoinDetailsRepository
import com.example.marketcoinslist.core.viewmodel.BaseViewModel
import com.example.marketcoinslist.core.viewmodel.DispatchersProvider
import com.example.marketcoinslist.core.viewmodel.Mapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CoinDetailsStateMapper : Mapper<CoinDetailsState, CoinDetailsState> {
    override fun map(s: CoinDetailsState, vararg args: Any?): CoinDetailsState = s
}

class CoinDetailsViewModel(
    private val repository: CoinDetailsRepository,
    dispatchers: DispatchersProvider,
    updater: CoinDetailsUpdater,
    processor: CoinDetailsProcessor
) : BaseViewModel<CoinDetailsState, CoinDetailsAction, CoinDetailsSideEffect, CoinDetailsEvent, CoinDetailsState>(
    initialState = CoinDetailsState(),
    dispatchers = dispatchers,
    updater = updater,
    processor = processor,
    stateUiMapper = CoinDetailsStateMapper()
) {
    private var coinId: String? = null

    fun init(id: String) {
        if (coinId == id) return
        coinId = id
        
        handle(CoinDetailsAction.LoadDetails(id))
        
        repository.getCoinDetails(id)
            .onEach { coin ->
                handle(CoinDetailsAction.CoinUpdated(coin))
            }
            .launchIn(viewModelScope)
    }
}