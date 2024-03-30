package com.rm.myrecipes.data

import com.rm.myrecipes.data.common.Result
import com.rm.myrecipes.data.common.SafeApiCall
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.RecipesEntity
import com.rm.myrecipes.domain.data.RecipeRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : RecipeRepository {

    override fun getRecipes(queries: Map<String, String>): Flow<RecipesResponse> = flow {
        when (val recipes =  SafeApiCall.call { remoteDataSource.getRecipes(queries) }) {
            is Result.NetworkError -> throw recipes.exception
            is Result.OK -> emit(recipes.data)
        }
    }.flowOn(dispatcher)

    override fun loadRecipes(): Flow<List<RecipesEntity>> = localDataSource.loadRecipes()

    override suspend fun insertRecipes(recipesEntity: RecipesEntity) = localDataSource.insertRecipes(recipesEntity)
}