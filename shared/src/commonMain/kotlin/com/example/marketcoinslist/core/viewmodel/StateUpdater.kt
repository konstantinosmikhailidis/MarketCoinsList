package com.example.marketcoinslist.core.viewmodel

/**
 * A pure function component responsible for state reduction in the MVI architecture.
 * 
 * It takes the current State and an incoming [Action], and returns a [StateHolder] containing:
 * - A newly computed State.
 * - Optional [SideEffect]s to be executed by the Processor.
 * - Optional one-off [Event]s to be consumed by the UI.
 */
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