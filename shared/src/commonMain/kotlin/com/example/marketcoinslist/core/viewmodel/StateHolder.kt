package com.example.marketcoinslist.core.viewmodel

data class StateHolder<State : Any, SideEffect : Any, Event : Any>(
    val state: State,
    val effects: Set<SideEffect> = emptySet(),
    val events: Set<Event> = emptySet()
)
