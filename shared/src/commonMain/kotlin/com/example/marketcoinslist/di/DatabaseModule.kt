package com.example.marketcoinslist.di

import com.example.marketcoinslist.database.MarketCoinsDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { MarketCoinsDatabase(get()) }
}