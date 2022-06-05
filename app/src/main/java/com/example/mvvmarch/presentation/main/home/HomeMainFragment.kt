package com.example.mvvmarch.presentation.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarch.R
import com.example.mvvmarch.databinding.FragmentMainHomeBinding
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.presentation.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeMainFragment : Fragment(R.layout.fragment_main_home) {

    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeMainViewModel by viewModels()
    private lateinit var rvAdapter: HomeProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainHomeBinding.bind(view)
        setUpRv()
        observerState()
        observeProducts()
        viewModel.fetchAllMyProducts()
        create()
    }

    private fun setUpRv() = binding.productsRecyclerView.apply {
        rvAdapter = HomeProductAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        adapter = rvAdapter

        rvAdapter.onItemClickListener = {
            navigateToDetail(it)
        }
    }

    private fun create() {
        binding.createFab.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_create)
        }
    }

    private fun navigateToDetail(productEntity: ProductEntity) {
        val bundle = bundleOf("product_id" to productEntity.id)
        findNavController().navigate(R.id.action_home_to_detail, bundle)
    }

    private fun observerState() {
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProducts() {
        viewModel.products.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { products -> handleProducts(products) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: HomeMainFragmentState) {
        when(state) {
            is HomeMainFragmentState.ShoToast -> requireContext().toast(state.message)
            is HomeMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeMainFragmentState.Init -> Unit
        }
    }

    private fun handleProducts(products: List<ProductEntity>) {
        if (products.isNotEmpty()) {
            rvAdapter.submitList(products)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.loadingProgressBar.isVisible = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}