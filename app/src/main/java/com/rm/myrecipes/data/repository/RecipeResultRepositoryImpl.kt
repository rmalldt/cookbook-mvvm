package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.common.Result
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResult
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResultEntity
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeResultRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: ResponseMapper
) : RecipeResultRepository {

    override fun getRecipeResult(fetchState: FetchState, query: Map<String, String>): Flow<RecipeResult> = flow {
        val res = when (fetchState) {
            is FetchState.FetchLocal -> {
                val localData = fetchRecipeResultFromLocal()
                if (localData.isNotEmpty()) localData.first() else fetchAndSaveRecipeResultRemote(query)
            }
            is FetchState.FetchRemote -> fetchAndSaveRecipeResultRemote(query)
            is FetchState.FetchSearch -> fetchSearchRecipesFromRemote(query)
        }
        emit(res)
    }

    private suspend fun fetchRecipeResultFromLocal(): List<RecipeResult> = localDataSource.getRecipeResult()
        .map { entity -> entity.toRecipeResult() }

    private suspend fun fetchAndSaveRecipeResultRemote (query: Map<String, String>): RecipeResult {
        val remoteData = when (val result = call { remoteDataSource.getRecipesResponse(query) }) {
            is Result.NetworkError -> throw result.exception
            is Result.OK -> mapper.toRecipeResult(result.data)
        }
        insertRecipeResult(remoteData.toRecipeResultEntity())
        return remoteData
    }

    private suspend fun fetchSearchRecipesFromRemote(query: Map<String, String>): RecipeResult {
        return when (val result = call { remoteDataSource.getRecipesResponse(query) }) {
            is Result.NetworkError -> throw result.exception
            is Result.OK -> mapper.toRecipeResult(result.data)
        }
    }

    private suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity) =
        localDataSource.insertRecipeResult(recipeResultEntity)

}
