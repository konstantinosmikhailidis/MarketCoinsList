package com.example.marketcoinslist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun rememberCoinNavigator(): NavBackStack<NavKey> {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Coins::class, Coins.serializer())
                    subclass(CoinDetails::class, CoinDetails.serializer())
                    subclass(WatchList::class, WatchList.serializer())
                }
            }
        },
        Coins
    )

    return backStack
}