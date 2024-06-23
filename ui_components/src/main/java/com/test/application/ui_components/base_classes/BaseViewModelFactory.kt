package com.test.application.ui_components.base_classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseViewModelFactory<V : ViewModel>(
    private val viewModelProducer: () -> V
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProducer() as T
    }
}