package com.test.application.feature_location.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.test.application.core.api.stations_list.StationsListProvider
import com.test.application.core.navigation.Router
import com.test.application.core.view.DataState
import com.test.application.feature_location.domain.model.City
import com.test.application.feature_location.domain.usecase.FindLocationUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
internal class LocationViewModel @AssistedInject constructor(
    @Assisted private val router: Router,
    private val findLocationUseCase: FindLocationUseCase,
    private val stationsListProvider: StationsListProvider
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            router: Router
        ): LocationViewModel
    }

    class LocationViewModelFactory @AssistedInject constructor(
        @Assisted private val router: Router,
        private val provider: Provider<LocationViewModel.Factory>
    ) : ViewModelProvider.Factory {

        @AssistedFactory
        interface Factory {
            fun create(
                router: Router
            ): LocationViewModelFactory
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when(modelClass) {
                LocationViewModel::class.java -> provider.get()
                    .create(router)

                else -> throw IllegalArgumentException(
                    "Create ViewModel Error for ${modelClass.name}. " +
                            "ModelClass must be ${this.javaClass.name}"
                )
            } as T
        }
    }

    private val _query = MutableSharedFlow<String?>(replay = 1)
    val query: SharedFlow<String?> = _query

    private val _locations = MutableStateFlow<DataState<List<City>>>(DataState.Loading())
    val locations: StateFlow<DataState<List<City>>> = _locations

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity

    init {
        viewModelScope.launch {
            query.filterNotNull()
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    flow {
                        emit(findLocationUseCase.invoke(query))
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    result.fold(
                        onSuccess = {places ->
                            _locations.value = DataState.Success(places)
                        },
                        onFailure = {error ->
                            _locations.value = DataState.Error(error)
                        }
                    )
                }
        }
    }

    fun updateQuery(query: String) {
        viewModelScope.launch {
            _query.emit(query)
        }
    }

    fun updateSelectedCity(cityName: String) {
        val city = _locations.value.let {
            if (it is DataState.Success) {
                it.data.find { city -> city.title == cityName }
            } else null
        }
        viewModelScope.launch {
            _selectedCity.emit(city)
        }
    }

    fun navigateToStationsList() {
        val city = _selectedCity.replayCache.firstOrNull()
        if (city != null) {
            val fragment = stationsListProvider.stationsListByCity(city.title)
            router.navigateTo(fragment)
        }
    }
}