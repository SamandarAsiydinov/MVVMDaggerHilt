package com.example.mvvmarch.domain.register

import com.example.mvvmarch.data.register.remote.dto.RegisterRequest
import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.register.entity.RegisterEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun registerRequest(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrapperResponse<RegisterResponse>>>

}