package com.example.marketcoinslist.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            networkModule,
            databaseModule,
            platformModule,
            repositoryModule,
            presentationModule
        )
    }

// called by iOS etc
fun initKoin() = initKoin {}