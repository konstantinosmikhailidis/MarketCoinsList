package com.example.marketcoinslist.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.marketcoinslist.database.MarketCoinsDatabase
import org.koin.dsl.module
import org.koin.core.module.Module

actual val platformModule: Module = module {
    single<SqlDriver> { 
        AndroidSqliteDriver(
            schema = MarketCoinsDatabase.Schema,
            context = get(),
            name = "marketcoins.db"
        )
    }
}