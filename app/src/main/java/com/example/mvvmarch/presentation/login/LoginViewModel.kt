package com.example.mvvmarch.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarch.data.login.remote.dto.LoginRequest
import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.login.entity.LoginEntity
import com.example.mvvmarch.domain.login.use_case.LoginUseCase
import com.example.mvvmarch.presentation.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val state: StateFlow<LoginActivityState> get() = _state

    private fun setLoading() {
        _state.value = LoginActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = LoginActivityState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _state.value = LoginActivityState.ShowToast(message)
    }

    private fun successLogin(loginEntity: LoginEntity) {
        _state.value = LoginActivityState.SuccessLogin(loginEntity)
    }

    private fun errorLogin(response: WrapperResponse<LoginResponse>) {
        _state.value = LoginActivityState.ErrorLogin(response)
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest)
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
                        is BaseResult.Success -> successLogin(result.data)
                        is BaseResult.Error -> errorLogin(result.rawResponse)
                    }
                }
        }
    }
}