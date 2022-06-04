package com.example.mvvmarch.presentation.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvmarch.R
import com.example.mvvmarch.data.product.remote.dto.ProductUpdateRequest
import com.example.mvvmarch.databinding.FragmentMainDetailBinding
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.presentation.common.extensions.toast
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltAndroidApp
class DetailMainFragment : Fragment(R.layout.fragment_main_detail) {

    private var _binding: FragmentMainDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainDetailBinding.bind(view)
        update()
        delete()
        observeState()
        observeProduct()
        fetchProductById()
    }

    private fun fetchProductById() {
        val id = arguments?.getInt("product_id") ?: 0
        viewModel.getProductById(id.toString())
    }

    private fun observeProduct() {
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeState() {
        viewModel.product.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleProduct(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: DetailMainFragmentState) {
        when (state) {
            is DetailMainFragmentState.SuccessDelete -> findNavController().navigate(R.id.action_detail_to_home)
            is DetailMainFragmentState.SuccessUpdate -> findNavController().navigate(R.id.action_detail_to_home)
            is DetailMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is DetailMainFragmentState.Init -> Unit
            is DetailMainFragmentState.ShowToast -> requireContext().toast(state.message)
        }
    }

    private fun handleProduct(productEntity: ProductEntity?) {
        productEntity.let { product ->
            binding.productNameEditText.setText(product?.name)
            binding.productPriceEditText.setText(product?.price.toString())
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.updateButton.isEnabled = !isLoading
        binding.deleteButton.isEnabled = !isLoading
    }

    private fun update() {
        binding.updateButton.setOnClickListener {
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.text.toString().trim()
            if (isValidate(name, price)) {
                val id = arguments?.getInt("product_id") ?: 0
                viewModel.updateProduct(ProductUpdateRequest(name, price.toInt()), id.toString())
            }
        }
    }

    private fun delete() {
        binding.deleteButton.setOnClickListener {
            val id = arguments?.getInt("product_id") ?: 0
            if (id != 0) {
                viewModel.deleteProduct(id.toString())
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