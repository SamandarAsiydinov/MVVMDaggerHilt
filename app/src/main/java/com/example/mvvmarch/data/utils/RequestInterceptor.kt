package com.example.mvvmarch.data.utils

import com.example.mvvmarch.manager.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val prefManager: SharedPrefManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefManager.getToken()
        val newReq = chain.request().newBuilder().addHeader("Authorization", token).build()
        return chain.proceed(newReq)
    }
}