package com.example.marketcoinslist.ui_element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay

/**
 * A custom, non-blocking floating notification view (Snackbar alternative).
 * 
 * It is anchored to the bottom of the screen using [Popup] with `focusable = false`.
 * This allows users to continue interacting with the underlying UI (e.g., scrolling the list)
 * while the notification is visible. Supports auto-dismissal.
 */
@Composable
fun NotificationView(
    isShowNotification: Boolean = true,
    message: String,
    isError: Boolean = true,
    autoCloseDelayMillis: Long = 3000L,
    errorColor: Color = Color(0xFFFDEDED),
    successfulColor: Color = Color(0xFFEDF7ED),
    clipCard: RoundedCornerShape = RoundedCornerShape(12.dp),
    messageColor: Color = Color(0xFF5F2120),
    messageStyle: TextStyle,
    iconPainter: Painter,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    Popup(
        alignment = Alignment.BottomCenter,
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            clippingEnabled = true
        )
    ) {
        LaunchedEffect(isShowNotification, message, isError) {
            if (isShowNotification && !isError) {
                delay(autoCloseDelayMillis)
                onDismissRequest()
            }
        }

        AnimatedVisibility(
            visible = isShowNotification,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)),
            modifier = modifier
        ) {
            val color = if (isError) errorColor else successfulColor
            Card(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .clip(clipCard),
                colors = CardDefaults.cardColors(
                    containerColor = color
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.weight(1f).padding(16.dp),
                        color = messageColor,
                        textAlign = TextAlign.Start,
                        style = messageStyle
                    )
                    if (isError) {
                        Icon(
                            painter = iconPainter,
                            contentDescription = iconContentDescription,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(18.dp)
                                .clickable { onDismissRequest() },
                            tint = messageColor
                        )
                    }
                }
            }
        }
    }
}