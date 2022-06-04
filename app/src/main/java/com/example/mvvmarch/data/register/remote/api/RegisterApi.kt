package com.example.mvvmarch.data.register.remote.api

import com.example.mvvmarch.data.register.remote.dto.RegisterRequest
import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.utils.Constants.REGISTER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {

    @POST(REGISTER)
    suspend fun register(@Body registerRequest: RegisterRequest): Response<WrapperResponse<RegisterResponse>>
}