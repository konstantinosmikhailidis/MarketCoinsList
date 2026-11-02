package com.example.marketcoinslist.di

import com.example.marketcoinslist.coin_list.data.repository.CoinRepositoryImpl
import com.example.marketcoinslist.coin_list.domain.repository.CoinRepository
import com.example.marketcoinslist.coin_details.data.repository.CoinDetailsRepositoryImpl
import com.example.marketcoinslist.coin_details.domain.repository.CoinDetailsRepository
import com.example.marketcoinslist.watch_list.data.repository.WatchListRepositoryImpl
import com.example.marketcoinslist.watch_list.domain.repository.WatchListRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CoinRepository> { CoinRepositoryImpl(get(), get()) }
    single<CoinDetailsRepository> { CoinDetailsRepositoryImpl(get(), get()) }
    single<WatchListRepository> { WatchListRepositoryImpl(get()) }
}