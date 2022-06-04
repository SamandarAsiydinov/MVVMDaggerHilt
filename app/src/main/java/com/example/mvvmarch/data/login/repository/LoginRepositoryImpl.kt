package com.example.mvvmarch.data.login.repository

import com.example.mvvmarch.data.login.remote.api.LoginApi
import com.example.mvvmarch.data.login.remote.dto.LoginRequest
import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.login.LoginRepository
import com.example.mvvmarch.domain.login.entity.LoginEntity
import com.example.mvvmarch.presentation.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi): LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrapperResponse<LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if (response.isSuccessful) {
                val body = response.body()!!
                val loginEntity = LoginEntity(body.data?.id!!, body.data?.name!!, body.data?.email!!, body.data?.token!!)
                emit(BaseResult.Success(loginEntity))
            } else {
                val type = object : TypeToken<WrapperResponse<LoginResponse>>() {}.type
                val error: WrapperResponse<LoginResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }
}