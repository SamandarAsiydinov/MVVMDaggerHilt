package com.example.mvvmarch.presentation.main.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvmarch.R
import com.example.mvvmarch.data.product.remote.dto.ProductCreateRequest
import com.example.mvvmarch.databinding.FragmentCreateMainBinding
import com.example.mvvmarch.presentation.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CreateMainFragment : Fragment(R.layout.fragment_create_main) {

    private var _binding: FragmentCreateMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateMainBinding.bind(view)

        observe()
        createProduct()
    }

    private fun observe() {
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: CreateMainFragmentState) {
        when(state) {
            is CreateMainFragmentState.IsLoading -> binding.saveButton.isEnabled = !state.isLoading
            is CreateMainFragmentState.Init -> Unit
            is CreateMainFragmentState.ShowToast -> requireContext().toast(state.message)
            is CreateMainFragmentState.SuccessCreate -> findNavController().navigate(R.id.action_create_to_home)
        }
    }

    private fun createProduct() {
        binding.saveButton.setOnClickListener {
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.text.toString().trim()
            if (isValidate(name, price)) {
                viewModel.createProduct(ProductCreateRequest(name, price.toInt()))
            }
        }
    }

    private fun setNameErr(e: String?) {
        binding.productNameInput.error = e
    }

    private fun setPriceErr(e: String?) {
        binding.productPriceInput.error = e
    }

    private fun isValidate(name: String, price: String): Boolean {
        clearErrors()
        if (name.isEmpty()) {
            setNameErr(getString(R.string.error_product_name_not_valid))
            return false
        }
        if (price.toIntOrNull() == null) {
            setPriceErr(getString(R.string.error_price_not_valid))
            return false
        }
        return true
    }

    private fun clearErrors() {
        setNameErr(null)
        setPriceErr(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}