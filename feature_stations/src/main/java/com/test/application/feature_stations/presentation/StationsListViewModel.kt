package com.test.application.feature_stations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.application.core.model.Charger
import com.test.application.core.navigation.Router
import com.test.application.feature_stations.domain.usecase.FindStationsUseCase
import com.test.application.ui_components.base_classes.BaseViewModel
import com.test.application.ui_components.base_classes.BaseViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import javax.inject.Provider

internal class StationsListViewModel @AssistedInject constructor(
    @Assisted private val router: Router,
    @Assisted private val selectedCity: String?,
    private val findsStationsUseCase: FindStationsUseCase
) : BaseViewModel<List<Charger>>() {
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
    ) : BaseViewModelFactory<StationsListViewModel>({
            provider.get().create(router, selectedCity)
        }) {

        @AssistedFactory
        interface Factory {
            fun create(
                router: Router,
                selectedCity: String?
            ): StationsListViewModelFactory
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                StationsListViewModel::class.java -> provider.get().create(router, selectedCity)
                else -> throw IllegalArgumentException(
                    "Create ViewModel Error for ${modelClass.name}. " +
                            "ModelClass must be ${this.javaClass.name}"
                )
            } as T
        }
    }

    init {
        loadStations()
    }

    private fun loadStations() {
        viewModelScope.launch {
            setLoadingState()
            selectedCity?.let {
                findsStationsUseCase.invoke(it)
                    .onSuccess { stationsList ->
                        val sortedList = stationsList.sortedBy { charger -> charger.name }
                        setSuccessState(sortedList)
                    }
                    .onFailure { error ->
                        setErrorState(error)
                    }
            } ?: run {
                val error = IllegalArgumentException("City is null")
                setErrorState(error)
            }
        }
    }

    fun onBackButtonPressed() {
        router.exit()
    }
}