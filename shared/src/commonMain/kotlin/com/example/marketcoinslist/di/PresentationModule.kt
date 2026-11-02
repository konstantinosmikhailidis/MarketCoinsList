package com.example.marketcoinslist.di

import com.example.marketcoinslist.core.viewmodel.DispatchersProvider
import com.example.marketcoinslist.core.viewmodel.DispatchersProviderBase
import com.example.marketcoinslist.coin_list.presentation.CoinsProcessor
import com.example.marketcoinslist.coin_list.presentation.CoinsUpdater
import com.example.marketcoinslist.coin_list.presentation.CoinsViewModel
import com.example.marketcoinslist.coin_details.presentation.CoinDetailsProcessor
import com.example.marketcoinslist.coin_details.presentation.CoinDetailsUpdater
import com.example.marketcoinslist.coin_details.presentation.CoinDetailsViewModel
import com.example.marketcoinslist.watch_list.presentation.WatchListProcessor
import com.example.marketcoinslist.watch_list.presentation.WatchListUpdater
import com.example.marketcoinslist.watch_list.presentation.WatchListViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.factoryOf

val presentationModule = module {
    single<DispatchersProvider> { DispatchersProviderBase() }
    
    // Coin List
    factoryOf(::CoinsUpdater)
    factoryOf(::CoinsProcessor)
    factoryOf(::CoinsViewModel)

    // Coin Details
    factoryOf(::CoinDetailsUpdater)
    factoryOf(::CoinDetailsProcessor)
    factoryOf(::CoinDetailsViewModel)

    // Watch List
    factoryOf(::WatchListUpdater)
    factoryOf(::WatchListProcessor)
    factoryOf(::WatchListViewModel)
}
