package com.example.marketcoinslist.core

interface Processor<out Action : Any, SideEffect : Any> {
    suspend fun process(effect: SideEffect): Action

    /**
     * Каждый процессор должен реализовать этот метод, чтобы указать,
     * какой именно Action возвращать при ошибке.
     */
    fun onError(message: String): Action

    suspend fun safeAction(
        defaultErrorMessage: String? = null,
        defaultMessage: String = "",
        useOnlyDefault: Boolean = false,
        block: suspend () -> @UnsafeVariance Action
    ): Action {
        return try {
            block()
        } catch (e: Exception) {
            val message = if (useOnlyDefault) {
                defaultErrorMessage ?: defaultMessage
            } else {
                e.message ?: defaultErrorMessage ?: defaultMessage
            }
            onError(message)
        }
    }
}