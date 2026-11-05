package com.example.marketcoinslist

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

fun openContent(activity: ComponentActivity) {
    activity.setContent {
        App()
    }
}