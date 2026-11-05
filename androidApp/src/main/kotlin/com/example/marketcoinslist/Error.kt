package com.example.marketcoinslist

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.marketcoinslist.error.CrashScreen

fun showErrorWindow(activity: ComponentActivity, clickCallback: () -> Unit) {
    activity.setContent {
        CrashScreen(
            clickCallback = clickCallback
        )
    }
}