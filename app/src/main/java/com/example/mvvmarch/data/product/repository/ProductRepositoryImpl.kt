package com.example.mvvmarch.data.product.repository

import com.example.mvvmarch.data.product.remote.api.ProductApi
import com.example.mvvmarch.data.product.remote.dto.ProductCreateRequest
import com.example.mvvmarch.data.product.remote.dto.ProductResponse
import com.example.mvvmarch.data.product.remote.dto.ProductUpdateRequest
import com.example.mvvmarch.data.utils.WrapperListResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.domain.product.ProductRepository
import com.example.mvvmarch.domain.product.entity.ProductEntity
import com.example.mvvmarch.domain.product.entity.ProductUserEntity
import com.example.mvvmarch.presentation.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productApi: ProductApi) :
    ProductRepository {
    override suspend fun getAllMyProducts(): Flow<BaseResult<List<ProductEntity>, WrapperListResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getAllMyProducts()
            if (response.isSuccessful) {
                val body = response.body()!!
                val products = mutableListOf<ProductEntity>()
                var user: ProductUserEntity?
                body.data?.forEach { product ->
                    user = ProductUserEntity(product.user.id, product.user.name, product.user.email)
                    products.add(
                        ProductEntity(
                            product.id,
                            product.name,
                            product.price,
                            user!!
                        )
                    )
                }
                emit(BaseResult.Success(products))
            } else {
                val type = object : TypeToken<WrapperListResponse<ProductResponse>>() {}.type
                val error: WrapperListResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun getProductById(id: String): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getProductById(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val user = ProductUserEntity(
                    body.data?.user?.id!!,
                    body.data?.user?.name!!,
                    body.data?.user?.email!!
                )
                val product =
                    ProductEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
                emit(BaseResult.Success(product))
            } else {
                val type = object : TypeToken<WrapperResponse<ProductResponse>>() {}.type
                val error: WrapperResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun updateProduct(
        productUpdateRequest: ProductUpdateRequest,
        id: String
    ): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>> {
        return flow {
            val response = productApi.updateProductById(productUpdateRequest, id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val user = ProductUserEntity(
                    body.data?.user?.id!!,
                    body.data?.user?.name!!,
                    body.data?.user?.email!!
                )
                val product =
                    ProductEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
                emit(BaseResult.Success(product))
            } else {
                val type = object : TypeToken<WrapperResponse<ProductResponse>>() {}.type
                val error: WrapperResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun deleteProductById(id: String): Flow<BaseResult<Unit, WrapperResponse<ProductResponse>>> {
        return flow {
            val response = productApi.deleteProduct(id)
            if (response.isSuccessful) {
                emit(BaseResult.Success(Unit))
            } else {
                val type = object : TypeToken<WrapperResponse<ProductResponse>>() {}.type
                val error: WrapperResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun createProduct(createProductRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrapperResponse<ProductResponse>>> {
        return flow {
            val response = productApi.createProduct(createProductRequest)
            if (response.isSuccessful) {
                val body = response.body()!!
                val user = ProductUserEntity(
                    body.data?.user?.id!!,
                    body.data?.user?.name!!,
                    body.data?.user?.email!!
                )
                val product =
                    ProductEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
                emit(BaseResult.Success(product))
            } else {
                val type = object : TypeToken<WrapperResponse<ProductResponse>>() {}.type
                val error: WrapperResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }
}