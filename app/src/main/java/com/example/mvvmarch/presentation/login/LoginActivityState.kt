package com.example.mvvmarch.presentation.login

import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.login.entity.LoginEntity

sealed class LoginActivityState {
    object Init: LoginActivityState()
    data class IsLoading(val isLoading: Boolean): LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val loginEntity: LoginEntity): LoginActivityState()
    data class ErrorLogin(val rawResponse: WrapperResponse<LoginResponse>) : LoginActivityState()
}
