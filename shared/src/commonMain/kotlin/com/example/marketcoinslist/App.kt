package com.example.marketcoinslist

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.marketcoinslist.navigation.CoinNavigation

@Composable
fun App() {
    MaterialTheme {
        CoinNavigation()
    }
}