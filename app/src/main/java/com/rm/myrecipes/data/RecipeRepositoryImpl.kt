package com.rm.myrecipes.data

import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.data.common.Result
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.data.network.MockCall
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.RecipeResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.RecipesEntity
import com.rm.myrecipes.data.room.RecipesEntity.Companion.toRecipes
import com.rm.myrecipes.data.room.RecipesEntity.Companion.toRecipesEntity
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeRepository
import com.rm.myrecipes.domain.data.Recipes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStoreRepository: DataStoreRepository,
    private val mapper: RecipeResponseMapper
) : RecipeRepository {

    override fun getRecipes(applied: Boolean): Flow<Recipes> = flow {
        val localData = loadRecipesFromLocal()
        val res = when {
            localData.isNotEmpty() && !applied -> localData.first()
            else -> fetchAndSave()
        }
        emit(res)
    }

    private suspend fun fetchAndSave(): Recipes {
        val remoteData = MockCall.fetchDummyRemote()
        insertRecipes(remoteData.toRecipesEntity())
        return remoteData
    }

    private fun loadRecipesFromLocal(): List<Recipes> = localDataSource.loadRecipes()
        .map { entity ->
            entity.toRecipes()
        }

    private suspend fun fetchRecipesFromRemote(): Recipes {
        val localePreferences = dataStoreRepository.data.first()
        val query = applyQueries(localePreferences.selectedMealType, localePreferences.selectedDietType)
        Timber.tag("Recipe").d("${query.entries}")
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

