package com.example.marketcoinslist.di

import com.example.marketcoinslist.core.network.createHttpClient
import com.example.marketcoinslist.coin_list.data.remote.CoinGeckoApi
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { CoinGeckoApi(get()) }
}