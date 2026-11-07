package com.example.marketcoinslist.core.viewmodel

/**
 * Handles asynchronous operations (SideEffects) in the MVI architecture.
 * 
 * This component is responsible for interacting with repositories (network, database, etc.)
 * and mapping the results back to an [Action] that will be processed by the [StateUpdater].
 */
interface Processor<out Action : Any, SideEffect : Any> {
    suspend fun process(effect: SideEffect): Action

    /**
     * Called when a generic error occurs (e.g., network failure, DB exception) during [safeAction].
     * Implementations must return an appropriate fallback [Action] (like showing an error notification).
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