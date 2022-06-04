package com.example.mvvmarch.domain.login

import com.example.mvvmarch.data.login.remote.dto.LoginRequest
import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.login.entity.LoginEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrapperResponse<LoginResponse>>>
}