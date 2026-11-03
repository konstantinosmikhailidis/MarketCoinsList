package com.example.marketcoinslist

import android.app.Application
import com.example.marketcoinslist.di.initKoin
import org.koin.android.ext.koin.androidContext

class MarketCoinsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MarketCoinsApplication)
        }
    }
}