package com.ysfcyln.mviplayground.ui

import com.ysfcyln.mviplayground.base.MviResult
import com.ysfcyln.mviplayground.base.MviIntent
import com.ysfcyln.mviplayground.base.MviState

/**
 * Contract of [MainActivity]
 *
 * See if you want to another approach for create copy of sealed class
 *
 * https://ivanmorgillo.com/2020/10/28/how-to-fix-the-pain-of-modifying-kotlin-nested-data-classes/
 * https://ivanmorgillo.com/2020/11/19/doh-there-is-no-copy-method-for-sealed-classes-in-kotlin/
 */
class MainContract {

    sealed class MyIntent : MviIntent {
        object OnRandomNumberClicked : MyIntent()
        object OnShowToastClicked : MyIntent()
    }

    data class State(
        val randomNumberState: RandomNumberState
    ) : MviState

    sealed class RandomNumberState {
        object Idle : RandomNumberState()
        object Loading : RandomNumberState()
        data class Success(val number : Int) : RandomNumberState()
    }

    sealed class Result : MviResult {

        object ShowToast : Result()

    }

}