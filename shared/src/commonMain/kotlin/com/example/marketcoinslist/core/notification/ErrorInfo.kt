package com.example.marketcoinslist.core.notification

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Error info
 *
 * Класс, необходимый для отображения ошибки или обычного оповещения
 *
 * @property message текст ошибки или оповещения
 * @property isError определяет это ошибка или простое оповещение
 * @property isCloseTimer определяет закрывать-ли оповещение через время
 */
@Serializable
data class ErrorInfo(
    val message: String,
    var isError: Boolean? = null,
    val isCloseTimer: Boolean = false
) {
    companion object {
        val Saver = Saver<ErrorInfo?, String>(
            save = { it?.let { Json.encodeToString(it) } },
            restore = {
                if (it == "null" || it.isBlank()) null
                else Json.decodeFromString(it)
            }
        )
    }
}
