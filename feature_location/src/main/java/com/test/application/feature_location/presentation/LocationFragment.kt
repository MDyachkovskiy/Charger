package com.test.application.feature_location.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.test.application.core.navigation.RouteProvider
import com.test.application.core.navigation.Router
import com.test.application.core.view.DataState
import com.test.application.feature_location.databinding.LocationSearchFragmentBinding
import com.test.application.feature_location.di.LocationComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

internal class LocationFragment: Fragment(), RouteProvider {

    @Inject
    lateinit var provider: Provider<LocationViewModel.LocationViewModelFactory.Factory>
    private val viewModel by viewModels<LocationViewModel> {
        provider.get().create(router)
    }

    private var _binding: LocationSearchFragmentBinding? = null
    private val binding get() = _binding!!
    override val router: Router
        get() = (requireActivity() as RouteProvider).router

    private var isItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationComponent.init(requireContext()).injectLocationFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationSearchFragmentBinding.inflate(inflater, container, false)
        setCloseButtonListener()
        setActionButtonListener()
        subscribeViewModel()
        initializeSearch()
        return binding.root
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

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            viewModel.locations.collect { state ->
                when (state) {
                    is DataState.Success -> {
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

                    is DataState.Error -> {
                        showError(state.throwable?.message ?: "Error")
                    }

                    is DataState.Loading -> {
                    }
                }
            }
        }
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
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!isItemSelected) {
                    p0?.toString()?.let { viewModel.updateQuery(it) }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if(isItemSelected) {
                    isItemSelected = false
                } else {
                    p0?.toString()?.let { query ->
                        val selectedCity = viewModel.locations.value.let { state ->
                            if (state is DataState.Success) {
                                state.data.find { it.title.equals(query, ignoreCase = true) }
                            } else null
                        }
                        if (selectedCity != null) {
                            viewModel.updateSelectedCity(selectedCity.title)
                        } else {
                            resetButtonText()
                        }
                    }
                }
            }
        })
    }

    private fun setupEditorActionListener() {
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchInput.text.toString()
                val selectedCity = viewModel.locations.value.let { state ->
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
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}