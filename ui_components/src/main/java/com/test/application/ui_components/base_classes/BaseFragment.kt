package com.test.application.ui_components.base_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias Inflate<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB
abstract class BaseFragment<VM : ViewModel, VB : ViewBinding, DS> (
    private val inflate: Inflate<VB>
): Fragment() {

    protected abstract val viewModel: VM
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        subscribeToViewModel()
        return binding.root
    }
    protected abstract fun handleDataState(state: DS)

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            getDataStateFlow().collect { state ->
                handleDataState(state)
            }
        }
    }

    protected abstract fun getDataStateFlow(): StateFlow<DS>

    protected fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}