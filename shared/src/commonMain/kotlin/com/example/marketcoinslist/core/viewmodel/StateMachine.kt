package com.example.marketcoinslist.core.viewmodel

interface StateMachine<State : Any, Action : Any, SideEffect : Any, Event : Any, UIState> :
    Handle<Action>,
    ObserveState<State>, ObserveEvents<Event>, ObserveUiState<UIState>