package com.example.marketcoinslist.core.notification

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.compose.runtime.saveable.Saver

@Serializable
data class LoadingInfo(
    val blockLoading: Boolean = false,
) {
    companion object {
        val Saver = Saver<LoadingInfo?, String>(
            save = { it?.let { Json.encodeToString(it) } },
            restore = {
                if (it == "null" || it.isBlank()) null
                else Json.decodeFromString(it)
            }
        )
    }
}