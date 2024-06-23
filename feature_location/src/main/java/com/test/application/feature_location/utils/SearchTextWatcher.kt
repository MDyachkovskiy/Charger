package com.test.application.feature_location.utils

import android.text.Editable
import android.text.TextWatcher
import com.test.application.core.view.DataState
import com.test.application.feature_location.domain.model.City
import com.test.application.feature_location.presentation.LocationViewModel

internal class SearchTextWatcher(
    private val viewModel: LocationViewModel,
    private val isItemSelectedCallback: () -> Boolean,
    private val setItemSelected: (Boolean) -> Unit,
    private val resetButtonText: () -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!isItemSelectedCallback()) {
            s?.toString()?.let { viewModel.updateQuery(it) }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (isItemSelectedCallback()) {
            setItemSelected(false)
        } else {
            s?.toString()?.let { query ->
                val selectedCity = getSelectedCity(query)
                if (selectedCity != null) {
                    viewModel.updateSelectedCity(selectedCity.title)
                } else {
                    resetButtonText()
                }
            }
        }
    }

    private fun getSelectedCity(query: String): City? {
        return viewModel.dataState.value.let { state ->
            if (state is DataState.Success) {
                state.data.find { it.title.equals(query, ignoreCase = true) }
            } else null
        }
    }
}