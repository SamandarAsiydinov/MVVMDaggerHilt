package com.example.mvvmarch.domain.login.use_case

import com.example.mvvmarch.data.login.remote.dto.LoginRequest
import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.login.LoginRepository
import com.example.mvvmarch.domain.login.entity.LoginEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrapperResponse<LoginResponse>>> {
        return loginRepository.login(loginRequest)
    }
}