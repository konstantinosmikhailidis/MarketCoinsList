package com.example.marketcoinslist.core

interface StateMachine<State : Any, Action : Any, SideEffect : Any, Event : Any, UIState> :
    Handle<Action>,
    ObserveState<State>, ObserveEvents<Event>, ObserveUiState<UIState>