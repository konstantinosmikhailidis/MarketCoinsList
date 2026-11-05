package com.example.marketcoinslist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import com.example.marketcoinslist.di.initKoin
import com.example.marketcoinslist.error.CrashScreen
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.Foundation.NSException
import platform.Foundation.NSSetUncaughtExceptionHandler
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.setUnhandledExceptionHook

private var isCrashState by mutableStateOf(false)
private var isHandlerSet = false
private var isKoinInitialized = false

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
private fun setupCrashHandler() {
    if (isHandlerSet) return
    isHandlerSet = true

    setUnhandledExceptionHook { _ ->
        dispatch_async(dispatch_get_main_queue()) {
            isCrashState = true
        }
    }

    NSSetUncaughtExceptionHandler(staticCFunction { _: NSException? ->
        dispatch_async(dispatch_get_main_queue()) {
            isCrashState = true
        }
    })
}

fun MainViewController() = ComposeUIViewController {
    setupCrashHandler()

    if (!isKoinInitialized) {
        initKoin()
        isKoinInitialized = true
    }

    if (isCrashState) {
        CrashScreen(
            clickCallback = {
                isCrashState = false
            }
        )
    } else {
        App()
    }
}