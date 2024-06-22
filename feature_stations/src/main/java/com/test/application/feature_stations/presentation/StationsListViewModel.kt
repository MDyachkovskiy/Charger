package com.test.application.feature_stations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.test.application.core.model.Charger
import com.test.application.core.navigation.Router
import com.test.application.core.view.DataState
import com.test.application.feature_stations.domain.usecase.FindStationsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Provider

internal class StationsListViewModel @AssistedInject constructor(
    @Assisted private val router: Router,
    @Assisted private val selectedCity: String?,
    private val findsStationsUseCase: FindStationsUseCase
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(
            router: Router,
            selectedCity: String?
        ): StationsListViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class StationsListViewModelFactory @AssistedInject constructor(
        @Assisted private val router: Router,
        @Assisted private val selectedCity: String?,
        private val provider: Provider<StationsListViewModel.Factory>
    ) : ViewModelProvider.Factory {

        @AssistedFactory
        interface Factory {
            fun create(
                router: Router,
                selectedCity: String?
            ): StationsListViewModelFactory
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                StationsListViewModel::class.java -> provider.get()
                    .create(router, selectedCity)

                else -> throw IllegalArgumentException(
                    "Create ViewModel Error for ${modelClass.name}. " +
                            "ModelClass must be ${this.javaClass.name}"
                )
            } as T
        }
    }

    private val _stations = MutableStateFlow<DataState<List<Charger>>>(DataState.Loading())
    val stations: StateFlow<DataState<List<Charger>>> = _stations

    init {
        loadStations()
    }

    private fun loadStations() {
        viewModelScope.launch {
            selectedCity?.let {
                findsStationsUseCase.invoke(it)
                    .onSuccess { stationsList ->
                        val sortedList = stationsList.sortedBy { charger -> charger.name }
                        _stations.value = DataState.Success(sortedList)
                    }
                    .onFailure { error ->
                        _stations.value = DataState.Error(error)
                    }
            } ?: run {
                val error = IllegalArgumentException("City is null")
                _stations.value = DataState.Error(error)
            }
        }
    }

    fun onBackButtonPressed() {
        router.exit()
    }
}