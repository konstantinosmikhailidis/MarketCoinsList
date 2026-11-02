package com.example.marketcoinslist.ui_element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.marketcoinslist.core.notification.LoadingInfo
import com.example.marketcoinslist.utils.isNotNull
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CircleLoader(
    loadingInfo: LoadingInfo?,
    modifier: Modifier = Modifier,
    minDisplayTimeMillis: Long = 1_000L,
    colorCircle: Color = Color.Blue
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

    AnimatedVisibility(
        visible = internalIsLoading
    ) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = loadingInfo?.blockLoading ?: false,
                dismissOnClickOutside = loadingInfo?.blockLoading ?: false
            )
        ) {
            ConfigureDialogWindow()
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorCircle,
                )
            }
        }
    }
}