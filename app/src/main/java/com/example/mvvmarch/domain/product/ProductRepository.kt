package com.example.mvvmarch.domain.product

import com.example.mvvmarch.data.product.remote.dto.ProductCreateRequest
import com.example.mvvmarch.data.product.remote.dto.ProductResponse
import com.example.mvvmarch.data.product.remote.dto.ProductUpdateRequest
import com.example.mvvmarch.data.utils.WrapperListResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllMyProducts(): Flow<BaseResult<List<ProductEntity>, WrapperListResponse<ProductResponse>>>
    suspend fun getProductById(id: String): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>>
    suspend fun updateProduct(productUpdateRequest: ProductUpdateRequest, id: String): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>>
    suspend fun deleteProductById(id: String): Flow<BaseResult<Unit, WrapperResponse<ProductResponse>>>
    suspend fun createProduct(createProductRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>>
}