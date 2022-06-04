package com.example.mvvmarch.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarch.data.register.remote.dto.RegisterRequest
import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.register.entity.RegisterEntity
import com.example.mvvmarch.domain.register.use_case.RegisterUseCase
import com.example.mvvmarch.presentation.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<RegisterActivityState> = MutableStateFlow(RegisterActivityState.Init)
    val state: StateFlow<RegisterActivityState> get() = _state

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            registerUseCase.invoke(request)
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
                        is BaseResult.Success -> {
                            successRegister(result.data)
                        }
                        is BaseResult.Error -> {
                            errorRequest(result.rawResponse)
                        }
                    }
                }
        }
    }

    private fun setLoading() {
        _state.value = RegisterActivityState.IsLoading(true)
    }
    private fun hideLoading() {
        _state.value = RegisterActivityState.IsLoading(false)
    }
    private fun showToast(message: String) {
        _state.value = RegisterActivityState.ShowToast(message)
    }
    private fun successRegister(registerEntity: RegisterEntity) {
        _state.value = RegisterActivityState.SuccessRegister(registerEntity)
    }
    private fun errorRequest(rawResponse: WrapperResponse<RegisterResponse>) {
        _state.value = RegisterActivityState.ErrorRegister(rawResponse)
    }
}