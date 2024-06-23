package com.test.application.feature_stations.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.application.core.model.Charger
import com.test.application.core.navigation.RouteProvider
import com.test.application.core.navigation.Router
import com.test.application.core.view.DataState
import com.test.application.feature_stations.databinding.StationsListFragmentBinding
import com.test.application.feature_stations.di.StationsListComponent
import com.test.application.ui_components.base_classes.BaseFragment
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Provider

internal class StationsListFragment:
    BaseFragment<StationsListViewModel, StationsListFragmentBinding, DataState<List<Charger>>>(
        StationsListFragmentBinding::inflate
    ),
    RouteProvider {

    @Inject
    lateinit var provider: Provider<StationsListViewModel.StationsListViewModelFactory.Factory>
    override val viewModel by viewModels<StationsListViewModel> {
        val selectedCity = arguments?.getSelectedCity()
        provider.get().create(router, selectedCity)
    }

    override val router: Router
        get() = (requireActivity() as RouteProvider).router

    private var adapter: StationsListAdapter? = null

    companion object {
        private const val SELECTED_CITY = "selected_city"

        fun newInstance(selectedCity: String): StationsListFragment {
            return StationsListFragment().apply {
                arguments = Bundle().apply {
                    putString(SELECTED_CITY, selectedCity)
                }
            }
        }

        private fun Bundle.getSelectedCity(): String? {
            return getString(SELECTED_CITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StationsListComponent.init(requireContext()).injectStationsFragment(this)
    }

    override fun getDataStateFlow(): StateFlow<DataState<List<Charger>>> {
        return viewModel.dataState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setCityName()
        setupBackButton()
    }

    override fun handleDataState(state: DataState<List<Charger>>) {
        when (state) {
            is DataState.Success -> {
                adapter?.submitList(state.data)
            }
            is DataState.Error -> {
                showError(state.throwable?.message ?: "Error")
            }
            is DataState.Loading -> {}
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            viewModel.onBackButtonPressed()
        }
    }

    private fun setCityName() {
        val cityName = arguments?.getSelectedCity()
        binding.placeName.text = cityName
    }

    private fun setupRecyclerView() {
        adapter = StationsListAdapter()
        binding.stationsList.adapter = adapter
        binding.stationsList.layoutManager = LinearLayoutManager(requireContext())
    }
}