package com.example.mvvmarch.domain.product.use_case

import com.example.mvvmarch.data.product.remote.dto.ProductResponse
import com.example.mvvmarch.data.product.remote.dto.ProductUpdateRequest
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.product.ProductRepository
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.presentation.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke(
        productUpdateRequest: ProductUpdateRequest,
        id: String
    ): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>> {
        return productRepository.updateProduct(productUpdateRequest, id)
    }
}