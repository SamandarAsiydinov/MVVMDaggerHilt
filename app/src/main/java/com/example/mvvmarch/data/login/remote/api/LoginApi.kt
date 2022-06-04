package com.example.mvvmarch.data.login.remote.api

import com.example.mvvmarch.data.login.remote.dto.LoginRequest
import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.utils.Constants.POST
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST(POST)
    suspend fun login(@Body loginRequest: LoginRequest): Response<WrapperResponse<LoginResponse>>
}