package com.rm.myrecipes.data

import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.data.common.Result
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.RecipeResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.RecipesEntity
import com.rm.myrecipes.data.room.RecipesEntity.Companion.toRecipes
import com.rm.myrecipes.data.room.RecipesEntity.Companion.toRecipesEntity
import com.rm.myrecipes.domain.data.RecipeRepository
import com.rm.myrecipes.domain.data.Recipes
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStoreRepository: DataStoreRepository,
    private val mapper: RecipeResponseMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    override fun getRecipes(): Flow<Recipes> =
        loadRecipesFromLocal()
            .map { localData ->
                if (localData.isNotEmpty()) {
                    localData.first()
                } else {
                    val localPreferences = dataStoreRepository.data.first()
                    val query = applyQueries(localPreferences.selectedMealType, localPreferences.selectedDietType)
                    val remoteData = fetchRecipesFromRemote(query)
                    insertRecipes(remoteData.toRecipesEntity())
                    remoteData
                }
            }.flowOn(dispatcher)

    private fun loadRecipesFromLocal(): Flow<List<Recipes>> = localDataSource.loadRecipes()
        .map { it.map { entity -> entity.toRecipes() } }


    private suspend fun fetchRecipesFromRemote(query: Map<String, String>): Recipes {
        return when (val result =  call { remoteDataSource.getRecipes(query) }) {
            is Result.NetworkError -> throw result.exception
            is Result.OK -> mapper.mapToRecipes(result.data)
        }
    }

    private suspend fun insertRecipes(recipesEntity: RecipesEntity) =
        localDataSource.insertRecipes(recipesEntity)

    private fun applyQueries(mealType: String, dietType: String): HashMap<String, String> {
        val query: HashMap<String, String> = hashMapOf()
        query[Constants.QUERY_NUMBER] = Constants.DEFAULT_QUERY_NUMBER
        query[Constants.QUERY_API_KEY] = Constants.API_KEY
        query[Constants.QUERY_TYPE] = mealType
        query[Constants.QUERY_DIET] = dietType
        query[Constants.QUERY_ADD_RECIPE_INFORMATION] = Constants.TRUE
        query[Constants.QUERY_FILL_INGREDIENTS] = Constants.TRUE
        return query
    }
}

