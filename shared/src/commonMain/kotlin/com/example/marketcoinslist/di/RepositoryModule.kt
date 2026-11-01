package com.example.marketcoinslist.di

import com.example.marketcoinslist.feature_coins.data.repository.CoinRepositoryImpl
import com.example.marketcoinslist.feature_coins.domain.repository.CoinRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CoinRepository> { CoinRepositoryImpl(get(), get()) }
}