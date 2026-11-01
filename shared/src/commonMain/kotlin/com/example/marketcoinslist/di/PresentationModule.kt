package com.example.marketcoinslist.di

import com.example.marketcoinslist.core.DispatchersProvider
import com.example.marketcoinslist.core.DispatchersProviderBase
import com.example.marketcoinslist.feature_coins.presentation.CoinsProcessor
import com.example.marketcoinslist.feature_coins.presentation.CoinsUpdater
import com.example.marketcoinslist.feature_coins.presentation.CoinsViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.factoryOf

val presentationModule = module {
    single<DispatchersProvider> { DispatchersProviderBase() }
    
    factoryOf(::CoinsUpdater)
    factoryOf(::CoinsProcessor)
    factoryOf(::CoinsViewModel)
}
