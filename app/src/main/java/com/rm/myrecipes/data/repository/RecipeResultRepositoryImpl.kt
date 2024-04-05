package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.RecipeResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResult
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResultEntity
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.data.common.Result
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeResultRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStoreRepository: DataStoreRepository,
    private val mapper: RecipeResponseMapper
) : RecipeResultRepository {

    override fun getRecipeResult(fetchState: FetchState): Flow<RecipeResult> = flow {
        val res = when (fetchState) {
            is FetchState.FetchLocal -> {
                val localData = fetchRecipeResultFromLocal()
                if (localData.isNotEmpty()) localData.first() else fetchAndSave()
            }
            is FetchState.FetchRemote -> fetchAndSave()
            is FetchState.FetchSearch -> fetchSearchRecipesFromRemote(fetchState.data)
        }
        emit(res)
    }

    private suspend fun fetchRecipeResultFromLocal(): List<RecipeResult> = localDataSource.getRecipeResult()
        .map { entity -> entity.toRecipeResult() }

    private suspend fun fetchAndSave(): RecipeResult {
        val remoteData = fetchRecipeResultFromRemote()
        insertRecipeResult(remoteData.toRecipeResultEntity())
        return remoteData
    }

    private suspend fun fetchRecipeResultFromRemote(): RecipeResult {
        val localePreferences = dataStoreRepository.data.first()
        val query = applyRecipeQuery(localePreferences.selectedMealType, localePreferences.selectedDietType)
        return when (val result =  call { remoteDataSource.getRecipesRemote(query) }) {
            is Result.NetworkError -> throw result.exception
            is Result.OK -> mapper.mapToRecipeResult(result.data)
        }
    }

    private suspend fun fetchSearchRecipesFromRemote(queryString: String): RecipeResult {
        val query = applySearchRecipeQuery(queryString)
        return when (val result = call { remoteDataSource.getRecipesRemote(query) }) {
            is Result.NetworkError -> throw result.exception
            is Result.OK -> mapper.mapToRecipeResult(result.data)
        }
    }

    private suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity) =
        localDataSource.insertRecipeResult(recipeResultEntity)

    private fun applyRecipeQuery(mealType: String, dietType: String): HashMap<String, String> {
        val query: HashMap<String, String> = hashMapOf()
        query[Constants.QUERY_NUMBER] = Constants.DEFAULT_QUERY_NUMBER
        query[Constants.QUERY_API_KEY] = Constants.API_KEY
        query[Constants.QUERY_TYPE] = mealType
        query[Constants.QUERY_DIET] = dietType
        query[Constants.QUERY_ADD_RECIPE_INFORMATION] = Constants.TRUE
        query[Constants.QUERY_FILL_INGREDIENTS] = Constants.TRUE
        return query
    }

    private fun applySearchRecipeQuery(searchString: String): HashMap<String, String> {
        val query: HashMap<String, String> = hashMapOf()
        query[Constants.QUERY_SEARCH] = searchString
        query[Constants.QUERY_NUMBER] = Constants.DEFAULT_QUERY_NUMBER
        query[Constants.QUERY_API_KEY] = Constants.API_KEY
        query[Constants.QUERY_ADD_RECIPE_INFORMATION] = Constants.TRUE
        query[Constants.QUERY_FILL_INGREDIENTS] = Constants.TRUE
        return query
    }
}
