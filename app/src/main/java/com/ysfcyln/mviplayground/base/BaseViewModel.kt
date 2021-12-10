package com.ysfcyln.mviplayground.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<Intent : MviIntent, State : MviState, Result : MviResult> : ViewModel() {

    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    val currentState: State
        get() = states.value

    private val _states : MutableStateFlow<State> = MutableStateFlow(initialState)
    val states = _states.asStateFlow()

    private val _intent : MutableSharedFlow<Intent> = MutableSharedFlow()
    val intent = _intent.asSharedFlow()

    private val _result : Channel<Result> = Channel()
    val result = _result.receiveAsFlow()


    init {
        subscribeIntents()
    }

    /**
     * Start listening to Intent
     */
    private fun subscribeIntents() {
        viewModelScope.launch {
            intent.collect {
                processIntents(it)
            }
        }
    }

    /**
     * Handle each event
     */
    abstract fun processIntents(intent : Intent)


    /**
     * Set new Intent
     */
    fun setIntent(intent : Intent) {
        val newIntent = intent
        viewModelScope.launch { _intent.emit(newIntent) }
    }


    /**
     * Set new Ui State
     */
    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _states.value = newState
    }

    /**
     * Set new Result
     */
    protected fun setResult(builder: () -> Result) {
        val effectValue = builder()
        viewModelScope.launch { _result.send(effectValue) }
    }
}