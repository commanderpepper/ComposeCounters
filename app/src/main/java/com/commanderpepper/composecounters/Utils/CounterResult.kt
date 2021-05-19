package com.commanderpepper.composecounters.Utils

sealed class CounterResult<out T : Any> {
    data class Success<out T : Any>(val result: T) : CounterResult<T>()
    class Error(val exception: Exception) : CounterResult<Nothing>()
    class Loading(val message: String = "Loading") : CounterResult<Nothing>()
}

