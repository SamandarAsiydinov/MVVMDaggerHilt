package com.example.mvvmarch.data.register.repository

import com.example.mvvmarch.data.register.remote.api.RegisterApi
import com.example.mvvmarch.data.register.remote.dto.RegisterRequest
import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.register.RegisterRepository
import com.example.mvvmarch.domain.register.entity.RegisterEntity
import com.example.mvvmarch.presentation.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerApi: RegisterApi): RegisterRepository {
    override suspend fun registerRequest(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrapperResponse<RegisterResponse>>> {
        return flow {
            val response = registerApi.register(registerRequest)
            if (response.isSuccessful) {
                val body = response.body()!!
                val loginEntity = RegisterEntity(body.data?.id!!, body.data?.name!!, body.data?.email!!, body.data?.token!!)
                emit(BaseResult.Success(loginEntity))
            } else {
                val type = object : TypeToken<WrapperResponse<RegisterResponse>>() {}.type
                val error: WrapperResponse<RegisterResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }
}