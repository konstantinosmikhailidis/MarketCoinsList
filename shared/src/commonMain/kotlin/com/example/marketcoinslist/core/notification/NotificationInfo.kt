package com.example.marketcoinslist.core.notification

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Model representing a UI notification (e.g., error, warning, or success message).
 *
 * This class encapsulates the data required to display a notification banner or snackbar.
 *
 * @property message The text message to be displayed.
 * @property isError Indicates whether this is an error (true), a success message (false), or neutral (null).
 * @property isCloseTimer Determines if the notification should auto-dismiss after a certain duration.
 */
@Serializable
data class NotificationInfo(
    val message: String,
    var isError: Boolean? = null,
    val isCloseTimer: Boolean = false
) {
    companion object {
        val Saver = Saver<NotificationInfo?, String>(
            save = { it?.let { Json.encodeToString(it) } },
            restore = {
                if (it == "null" || it.isBlank()) null
                else Json.decodeFromString(it)
            }
        )
    }
}
