package com.test.application.feature_location.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.test.application.core.navigation.RouteProvider
import com.test.application.core.navigation.Router
import com.test.application.core.view.DataState
import com.test.application.feature_location.databinding.LocationSearchFragmentBinding
import com.test.application.feature_location.di.LocationComponent
import com.test.application.feature_location.domain.model.City
import com.test.application.feature_location.utils.SearchTextWatcher
import com.test.application.ui_components.base_classes.BaseFragment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

internal class LocationFragment:
    BaseFragment<LocationViewModel, LocationSearchFragmentBinding, DataState<List<City>>>(
        LocationSearchFragmentBinding::inflate
    ),
    RouteProvider {

    @Inject
    lateinit var provider: Provider<LocationViewModel.LocationViewModelFactory.Factory>
    override val viewModel by viewModels<LocationViewModel> {
        provider.get().create(router)
    }

    override val router: Router
        get() = (requireActivity() as RouteProvider).router

    private var isItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationComponent.init(requireContext()).injectLocationFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCloseButtonListener()
        setActionButtonListener()
        initializeSearch()
    }

    override fun getDataStateFlow(): StateFlow<DataState<List<City>>> {
        return viewModel.dataState
    }

    override fun handleDataState(state: DataState<List<City>>) {
        when (state) {
            is DataState.Success -> handleSuccessState(state)
            is DataState.Error -> showError(state.throwable?.message ?: "Error")
            is DataState.Loading -> {}
        }
    }

    private fun handleSuccessState(state: DataState.Success<List<City>>) {
        val cityList = state.data.map { it.title }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, cityList
        )
        binding.searchInput.setAdapter(adapter)
        if (!isItemSelected) {
            binding.searchInput.showDropDown()
        }
    }

    private fun setActionButtonListener() {
        binding.actionButton.setOnClickListener {
            viewModel.navigateToStationsList()
        }
    }

    private fun setCloseButtonListener() {
        binding.apply {
            close.setOnClickListener {
                hideKeyboard()
                binding.searchInput.setText("")
                resetButtonText()
                binding.searchInput.requestFocus()
            }
        }
    }

    private fun resetButtonText() {
        binding.buttonText.text = "Показать станции"
        binding.actionButton.requestLayout()
    }
    private fun initializeSearch() {
        setupTextWatcher()
        setupEditorActionListener()
        setupCollectors()
        setupItemClickListener()
    }

    private fun setupItemClickListener() {
        binding.searchInput.setOnItemClickListener { _, _, position, _ ->
            isItemSelected = true
            val selectedCity = (binding.searchInput.adapter.getItem(position) as String)
            viewModel.updateSelectedCity(selectedCity)
            hideKeyboard()
            binding.searchInput.clearFocus()
        }
    }

    private fun setupTextWatcher() {
        binding.searchInput.addTextChangedListener(SearchTextWatcher(
            viewModel,
            {isItemSelected}, {isItemSelected = it}, {resetButtonText()}
        ))
    }

    private fun setupEditorActionListener() {
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchInput.text.toString()
                val selectedCity = viewModel.dataState.value.let { state ->
                    if (state is DataState.Success) {
                        state.data.find { it.title.equals(query, ignoreCase = true) }
                    } else null
                }
                if(selectedCity !=null) {
                    viewModel.updateSelectedCity(selectedCity.title)
                } else {
                    resetButtonText()
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        binding.searchInput.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    private fun setupCollectors(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.query.collectLatest { query ->
                if (binding.search.editText?.text.toString() != query) {
                    binding.search.editText?.setText(query)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedCity.collectLatest { city ->
                if (city != null) {
                    binding.buttonText.text = "Показать ${city.chargerCount} станции"
                    binding.actionButton.requestLayout()
                } else {
                    resetButtonText()
                }
            }
        }
    }
}