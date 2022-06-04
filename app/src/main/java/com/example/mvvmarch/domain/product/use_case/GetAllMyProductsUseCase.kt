package com.example.mvvmarch.domain.product.use_case

import com.example.mvvmarch.data.product.remote.dto.ProductResponse
import com.example.mvvmarch.data.utils.WrapperListResponse
import com.example.mvvmarch.domain.product.ProductRepository
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMyProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke(): Flow<BaseResult<List<ProductEntity>, WrapperListResponse<ProductResponse>>> {
        return productRepository.getAllMyProducts()
    }
}