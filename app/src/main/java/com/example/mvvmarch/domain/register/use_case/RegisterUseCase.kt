package com.example.mvvmarch.domain.register.use_case

import com.example.mvvmarch.data.register.remote.dto.RegisterRequest
import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.register.RegisterRepository
import com.example.mvvmarch.domain.register.entity.RegisterEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    suspend fun invoke(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrapperResponse<RegisterResponse>>> {
        return registerRepository.registerRequest(registerRequest)
    }
}