package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.common.handleResponse
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResult
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeResultRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: ResponseMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RecipeResultRepository {

    override suspend fun loadRecipeResultLocal(): List<RecipeResult> =
        withContext(dispatcher) {
            localDataSource.getRecipeResult()
                .map { entity -> entity.toRecipeResult() }
        }

    override suspend fun fetchRecipeResultRemote(query: Map<String, String>): Result<RecipeResult> =
        withContext(dispatcher) {
            handleResponse(
                request = { remoteDataSource.getRecipesResponse(query) },
                responseMapper = { mapper.toRecipeResult(it) }
            )
        }

    override suspend fun fetchSearchedRecipesResult(query: Map<String, String>): Result<RecipeResult> =
        withContext(dispatcher) {
            handleResponse(
                request = { remoteDataSource.getRecipesResponse(query) },
                responseMapper = { mapper.toRecipeResult(it) }
            )
        }

    override suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity) =
        withContext(dispatcher) {
            localDataSource.insertRecipeResult(recipeResultEntity)
        }
}
