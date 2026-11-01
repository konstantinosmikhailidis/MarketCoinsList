package com.example.marketcoinslist.di

import com.example.marketcoinslist.core.network.createHttpClient
import com.example.marketcoinslist.feature_coins.data.remote.CoinGeckoApi
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { CoinGeckoApi(get()) }
}