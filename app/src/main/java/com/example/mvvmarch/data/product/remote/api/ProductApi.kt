package com.example.mvvmarch.data.product.remote.api

import com.example.mvvmarch.data.product.remote.dto.ProductCreateRequest
import com.example.mvvmarch.data.product.remote.dto.ProductResponse
import com.example.mvvmarch.data.product.remote.dto.ProductUpdateRequest
import com.example.mvvmarch.data.utils.WrapperListResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.utils.Constants.PRODUCT
import com.example.mvvmarch.utils.Constants.PRODUCT_BY_ID
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {

    @GET(PRODUCT)
    suspend fun getAllMyProducts(): Response<WrapperListResponse<ProductResponse>>

    @GET(PRODUCT_BY_ID)
    suspend fun getProductById(@Path("id") id: String): Response<WrapperResponse<ProductResponse>>

    @PUT(PRODUCT_BY_ID)
    suspend fun updateProductById(@Body productUpdateRequest: ProductUpdateRequest, @Path("id") id: String): Response<WrapperResponse<ProductResponse>>

    @DELETE(PRODUCT_BY_ID)
    suspend fun deleteProduct(@Path("id") id: String): Response<WrapperResponse<ProductResponse>>

    @POST(PRODUCT)
    suspend fun createProduct(@Body productCreateRequest: ProductCreateRequest): Response<WrapperResponse<ProductResponse>>
}