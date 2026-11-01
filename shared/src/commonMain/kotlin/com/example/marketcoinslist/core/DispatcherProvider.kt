package com.example.marketcoinslist.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface DispatchersProvider {

    fun async(scope: CoroutineScope, block: suspend () -> Unit) : Job

    fun main(scope: CoroutineScope, block: suspend () -> Unit) : Job

    suspend fun switchToMain(block: suspend () -> Unit)
}

abstract class DispatchersProviderImpl(private val ui : CoroutineDispatcher, private val background : CoroutineDispatcher) :
    DispatchersProvider {

    override fun async(scope: CoroutineScope, block: suspend () -> Unit) : Job {
        return scope.launch(background) {
            block()
        }
    }

    override fun main(scope: CoroutineScope, block: suspend () -> Unit): Job {
        return scope.launch(ui) {
            block()
        }
    }

    override suspend fun switchToMain(block: suspend () -> Unit) {
        withContext(ui) {
            block()
        }
    }
}

class DispatchersProviderBase: DispatchersProviderImpl(Dispatchers.Main, Dispatchers.Default)