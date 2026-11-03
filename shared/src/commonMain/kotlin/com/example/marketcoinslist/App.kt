package com.example.marketcoinslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.marketcoinslist.di.initKoin
import com.example.marketcoinslist.navigation.rememberCoinNavigator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import com.example.marketcoinslist.navigation.CoinList
import com.example.marketcoinslist.navigation.CoinDetails
import com.example.marketcoinslist.navigation.WatchList
import com.example.marketcoinslist.coin_list.presentation.ui.CoinListScreen
import com.example.marketcoinslist.coin_details.presentation.ui.CoinDetailsScreen
import com.example.marketcoinslist.watch_list.presentation.ui.WatchListScreen
import org.koin.compose.KoinContext

@Composable
fun App() {
    MaterialTheme {
        KoinContext {
            val navigator = rememberCoinNavigator()
            
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                NavDisplay(
                    backStack = navigator,
                    onBack = { navigator.removeLastOrNull() },
                    entryProvider = { key ->
                        when (key) {
                            is CoinList -> NavEntry(key) {
                                CoinListScreen(
                                    onNavigateToDetails = { id ->
                                        navigator.add(CoinDetails(id))
                                    }
                                )
                            }
                            is CoinDetails -> NavEntry(key) {
                                CoinDetailsScreen()
                            }
                            is WatchList -> NavEntry(key) {
                                WatchListScreen()
                            }
                            else -> error("Unknown route: $key")
                        }
                    }
                )
            }
        }
    }
}