package com.test.application.feature_stations.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.application.core.navigation.RouteProvider
import com.test.application.core.navigation.Router
import com.test.application.core.view.DataState
import com.test.application.feature_stations.databinding.StationsListFragmentBinding
import com.test.application.feature_stations.di.StationsListComponent
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

internal class StationsListFragment: Fragment(), RouteProvider {

    @Inject
    lateinit var provider: Provider<StationsListViewModel.StationsListViewModelFactory.Factory>
    private val viewModel by viewModels<StationsListViewModel> {
        val selectedCity = arguments?.getSelectedCity()
        provider.get().create(
            router, parentFragmentManager, selectedCity
        )
    }

    override val router: Router
        get() = (requireActivity() as RouteProvider).router

    private var _binding: StationsListFragmentBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StationsListFragmentBinding.inflate(inflater, container, false)
        setupRecyclerView()
        subscribeToViewModel()
        setCityName()
        setupBackButton()
        return binding.root
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

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stations.collect { state ->
                when (state) {
                    is DataState.Success -> {
                        adapter?.submitList(state.data)
                    }
                    is DataState.Error -> {
                        val errorMessage = state.throwable?.message ?: "Error"
                        showError(errorMessage)
                    }
                    is DataState.Loading -> {}
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StationsListAdapter()
        binding.stationsList.adapter = adapter
        binding.stationsList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}