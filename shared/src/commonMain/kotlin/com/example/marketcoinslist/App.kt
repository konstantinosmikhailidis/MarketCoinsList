package com.example.marketcoinslist

import androidx.compose.runtime.Composable
import com.example.marketcoinslist.navigation.CoinNavigation
import com.example.marketcoinslist.theme.CoinTheme

@Composable
fun App() {
    CoinTheme {
        CoinNavigation()
    }
}