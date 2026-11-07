package com.example.marketcoinslist.core.viewmodel

interface StateUpdater<S : Any, in Action : Any, SideEffect : Any, Event : Any> {
    fun update(action: Action, currentState: S): StateHolder<S, SideEffect, Event>

    abstract class Abstract<S : Any, Action : CoinAction, SideEffect : Any, Event : Any> :
        StateUpdater<S, Action, SideEffect, Event>
    {
        override fun update(action: Action, currentState: S): StateHolder<S, SideEffect, Event> {
            if (action is CoinAction.Nothing) {
                return StateHolder(currentState)
            } else {
                throw IllegalStateException("Unsupported action")
            }
        }
    }
}