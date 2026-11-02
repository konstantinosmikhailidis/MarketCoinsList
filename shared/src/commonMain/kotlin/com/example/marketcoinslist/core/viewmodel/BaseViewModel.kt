package com.example.marketcoinslist.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<State : Any, Action : Any, SideEffect : Any, Event : Any, UIState>(
    initialState: State,
    private val dispatchers: DispatchersProvider,
    private val initialEffects: Set<SideEffect> = emptySet(),
    private val initialEvents: Set<Event> = emptySet(),
    private val updater: StateUpdater<State, Action, SideEffect, Event>,
    private val processor: Processor<Action, SideEffect>,
    private val stateUiMapper: Mapper<State, UIState>? = null,
    private val coroutineExceptionHandler: CoroutineExceptionHandler? = null
) : ViewModel(), StateMachine<State, Action, SideEffect, Event, UIState> {
    private val events = MutableSharedFlow<Event>()
    protected val state = MutableStateFlow(initialState)
    private val uiState by lazy { MutableStateFlow(stateUiMapper?.map(state.value)) }

    init {
        processInitialEvents(initialEvents)
        processEffects(initialEffects)
    }

    final override fun handle(data: Action) {
        val newState = updater.update(data, state.value)

        if (newState.effects.isNotEmpty()) {
            processEffects(newState.effects)
        }

        if (newState.events.isNotEmpty()) {
            main {
                newState.events.forEach {
                    events.emit(it)
                }
            }
        }

        state.value = newState.state
        uiState.value = stateUiMapper?.map(newState.state)
    }

    final override fun observeEvent(): SharedFlow<Event> {
        return events.asSharedFlow()
    }

    @Suppress("UNCHECKED_CAST")
    final override fun observeUiState(): StateFlow<UIState> {
        check(stateUiMapper != null) { "Define stateUiMapper to use UIState" }
        return uiState.asStateFlow() as StateFlow<UIState>
    }

    final override fun observeState(): StateFlow<State> {
        return state.asStateFlow()
    }

    protected fun async(block: suspend () -> Unit) {
        viewModelScope.launch {
            dispatchers.async(viewModelScope) {
                block()
            }
        }
    }

    protected fun main(block: suspend () -> Unit) {
        viewModelScope.launch {
            dispatchers.main(viewModelScope) {
                block()
            }
        }
    }

    fun processEffects(effects: Set<SideEffect>) {
        effects.forEach { effect ->
            val coroutineContext = coroutineExceptionHandler ?: EmptyCoroutineContext

            viewModelScope.launch(coroutineContext) {
                dispatchers.async(viewModelScope) {
                    val newAction = processor.process(effect)

                    dispatchers.switchToMain {
                        handle(newAction)
                    }
                }
            }
        }
    }

    private fun processInitialEvents(eventsList: Set<Event>) {
        if (eventsList.isEmpty()) return
        main {
            eventsList.forEach {
                events.emit(it)
            }
        }
    }
}