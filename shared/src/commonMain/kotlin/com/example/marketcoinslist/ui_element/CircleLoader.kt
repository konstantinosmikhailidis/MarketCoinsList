package com.example.marketcoinslist.ui_element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.utils.isNotNull
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

/**
 * A non-blocking, centered loading indicator.
 * 
 * Uses a [Popup] rather than a Dialog to ensure that it doesn't block user interactions
 * (like scrolling or clicking back) while background data is being fetched. It also implements
 * a minimum display time to prevent UI flickering on very fast network responses.
 */
@Composable
fun CircleLoader(
    loadingInfo: LoadingInfo?,
    modifier: Modifier = Modifier,
    minDisplayTimeMillis: Long = 1_000L,
    colorCircle: Color = MaterialTheme.colorScheme.primary
) {
    var internalIsLoading by remember { mutableStateOf(false) }
    var loaderStartTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(loadingInfo.isNotNull()) {
        if (loadingInfo.isNotNull()) {
            internalIsLoading = true
            loaderStartTime = Clock.System.now().toEpochMilliseconds()
        } else {
            if (internalIsLoading) {
                val elapsedTime = Clock.System.now().toEpochMilliseconds() - loaderStartTime
                val remainingTime = minDisplayTimeMillis - elapsedTime

                if (remainingTime > 0) {
                    delay(remainingTime.milliseconds)
                }
                internalIsLoading = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {}
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = internalIsLoading,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                CircularProgressIndicator(
                    color = colorCircle,
                    modifier = modifier
                )
            }
        }
    }
}