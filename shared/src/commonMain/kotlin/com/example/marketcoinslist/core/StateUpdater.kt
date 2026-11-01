package com.example.marketcoinslist.core

interface StateUpdater<S : Any, in Action : Any, SideEffect : Any, Event : Any> {
    fun update(action: Action, currentState: S): StateHolder<S, SideEffect, Event>

    abstract class Abstract<S : Any, Action : Actions, SideEffect : Any, Event : Any> :
        StateUpdater<S, Action, SideEffect, Event>
    {
        override fun update(action: Action, currentState: S): StateHolder<S, SideEffect, Event> {
            if (action is Actions.Nothing) {
                return StateHolder(currentState)
            } else {
                throw IllegalStateException("Unsupported action")
            }
        }
    }
}