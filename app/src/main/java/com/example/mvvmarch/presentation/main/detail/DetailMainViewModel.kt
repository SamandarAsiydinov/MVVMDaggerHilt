package com.example.mvvmarch.presentation.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarch.data.product.remote.dto.ProductUpdateRequest
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.domain.product.use_case.DeleteProductUseCase
import com.example.mvvmarch.domain.product.use_case.GetProductByIdUseCase
import com.example.mvvmarch.domain.product.use_case.UpdateProductUseCase
import com.example.mvvmarch.presentation.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMainViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase
): ViewModel() {
    private val _state: MutableStateFlow<DetailMainFragmentState> = MutableStateFlow(DetailMainFragmentState.Init)
    val state: StateFlow<DetailMainFragmentState> get() = _state

    private val _product = MutableStateFlow<ProductEntity?>(null)
    val product: StateFlow<ProductEntity?> get() = _product

    private fun setLoading(isLoading: Boolean) {
        _state.value = DetailMainFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String) {
        _state.value = DetailMainFragmentState.ShowToast(message)
    }
    fun getProductById(id: String) {
        viewModelScope.launch {
            getProductByIdUseCase.invoke(id)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    showToast(it.stackTraceToString())
                }
                .collect { result ->
                    setLoading(false)
                    when(result) {
                        is BaseResult.Success -> _product.value = result.data
                        is BaseResult.Error -> showToast(result.rawResponse.message)
                    }
                }
        }
    }
    fun updateProduct(productUpdateRequest: ProductUpdateRequest, id: String) {
        viewModelScope.launch {
            updateProductUseCase.invoke(productUpdateRequest, id)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    showToast(it.stackTraceToString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> _state.value = DetailMainFragmentState.SuccessUpdate
                        is BaseResult.Error -> showToast(result.rawResponse.message)
                    }
                }
        }
    }
    fun deleteProduct(id: String) {
        viewModelScope.launch {
            deleteProductUseCase.invoke(id)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    showToast(it.stackTraceToString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _state.value = DetailMainFragmentState.SuccessDelete
                        is BaseResult.Error -> showToast(result.rawResponse.message)
                    }
                }
        }
    }
}