package com.ysfcyln.mviplayground.ui

import androidx.lifecycle.viewModelScope
import com.ysfcyln.mviplayground.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel<MainContract.MyIntent, MainContract.State, MainContract.Result>() {


    /**
     * Create initial State of Views
     */
    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            MainContract.RandomNumberState.Idle
        )
    }

    /**
     * Handle each event
     */
    override fun processIntents(intent: MainContract.MyIntent) {
        when (intent) {
            is MainContract.MyIntent.OnRandomNumberClicked -> { generateRandomNumber() }
            is MainContract.MyIntent.OnShowToastClicked -> {
                setResult { MainContract.Result.ShowToast }
            }
        }
    }


    /**
     * Generate a random number
     */
    private fun generateRandomNumber() {
        viewModelScope.launch {
            // Set Loading
            setState { copy(randomNumberState = MainContract.RandomNumberState.Loading) }
            try {
                delay(5000)
                val random = (0..10).random()
                if (random % 2 == 0) {
                    setState { copy(randomNumberState = MainContract.RandomNumberState.Idle) }
                    throw RuntimeException("Number is even")
                }
                setState { copy(randomNumberState = MainContract.RandomNumberState.Success(number = random)) }
            } catch (exception : Exception) {
                setResult { MainContract.Result.ShowToast }
            }
        }
    }
}