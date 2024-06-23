package com.test.application.ui_components.base_classes

import androidx.lifecycle.ViewModel
import com.test.application.core.view.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T> : ViewModel() {
    protected val _dataState: MutableStateFlow<DataState<T>> = MutableStateFlow(DataState.Loading())
    val dataState: StateFlow<DataState<T>> get() = _dataState

    protected fun setLoadingState() {
        _dataState.value = DataState.Loading()
    }

    protected fun setSuccessState(data: T) {
        _dataState.value = DataState.Success(data)
    }

    protected fun setErrorState(exception: Throwable) {
        _dataState.value = DataState.Error(exception)
    }
}