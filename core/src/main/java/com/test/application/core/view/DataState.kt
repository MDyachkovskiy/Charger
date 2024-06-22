package com.test.application.core.view

sealed interface DataState<T> {

    class Loading<T> : DataState<T>

    data class Success<T> (
        val data: T
    ) : DataState<T>

    data class Error<T> (
        val throwable: Throwable? = null
    ) : DataState<T>
}