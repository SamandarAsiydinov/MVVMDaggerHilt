package com.example.mvvmarch.presentation.register

import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.register.entity.RegisterEntity

sealed class RegisterActivityState {
    object Init: RegisterActivityState()
    data class IsLoading(val isLoading: Boolean): RegisterActivityState()
    data class ShowToast(val message: String): RegisterActivityState()
    data class SuccessRegister(val registerEntity: RegisterEntity): RegisterActivityState()
    data class ErrorRegister(val rawResponse: WrapperResponse<RegisterResponse>): RegisterActivityState()
}
