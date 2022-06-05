package com.example.mvvmarch.presentation.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarch.data.product.remote.dto.ProductCreateRequest
import com.example.mvvmarch.domain.product.use_case.CreateProductUserCase
import com.example.mvvmarch.presentation.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMainViewModel @Inject constructor(
    private val createProductUserCase: CreateProductUserCase
) : ViewModel() {
    private val _state: MutableStateFlow<CreateMainFragmentState> =
        MutableStateFlow(CreateMainFragmentState.Init)
    val state: StateFlow<CreateMainFragmentState> get() = _state

    private fun setLoading(b: Boolean) {
        _state.value = CreateMainFragmentState.IsLoading(b)
    }
    private fun showToast(message: String) {
        _state.value = CreateMainFragmentState.ShowToast(message)
    }
    fun createProduct(createRequest: ProductCreateRequest) {
        viewModelScope.launch {
            createProductUserCase.invoke(createRequest)
                .onStart {
                    setLoading(true)
                }
                .catch {
                    setLoading(false)
                    showToast(it.stackTraceToString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            _state.value = CreateMainFragmentState.SuccessCreate
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}