package com.example.mvvmarch.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.domain.product.use_case.GetAllMyProductsUseCase
import com.example.mvvmarch.presentation.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(private val getAllMyProductsUseCase: GetAllMyProductsUseCase) :
    ViewModel() {
    private val _state: MutableStateFlow<HomeMainFragmentState> =
        MutableStateFlow(HomeMainFragmentState.Init)
    val state: StateFlow<HomeMainFragmentState> get() = _state

    private val _products: MutableStateFlow<List<ProductEntity>> = MutableStateFlow(mutableListOf())
    val products: StateFlow<List<ProductEntity>> get() = _products

    private fun setLoading() {
        _state.value = HomeMainFragmentState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = HomeMainFragmentState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _state.value = HomeMainFragmentState.ShoToast(message)
    }

    fun fetchAllMyProducts() {
        viewModelScope.launch {
            getAllMyProductsUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                    showToast(it.stackTraceToString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> _products.value = result.data
                        is BaseResult.Error -> showToast(result.rawResponse.message)
                    }
                }
        }
    }
}