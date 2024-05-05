package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.data.common.Constants.Companion.API_KEY
import com.rm.myrecipes.data.common.Constants.Companion.DEFAULT_QUERY_NUMBER
import com.rm.myrecipes.data.common.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.rm.myrecipes.data.common.Constants.Companion.QUERY_API_KEY
import com.rm.myrecipes.data.common.Constants.Companion.QUERY_DIET
import com.rm.myrecipes.data.common.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.rm.myrecipes.data.common.Constants.Companion.QUERY_NUMBER
import com.rm.myrecipes.data.common.Constants.Companion.QUERY_TYPE
import com.rm.myrecipes.data.common.Constants.Companion.TRUE
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResultEntity
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import com.rm.myrecipes.ui.common.FetchType
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeResultRepository: RecipeResultRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(fetchType: FetchType): Result<RecipeResult> {
        return when (fetchType) {
            is FetchType.Local -> {
                val localResult = recipeResultRepository.loadRecipeResultLocal()
                if (localResult.isNotEmpty()) {
                    Result.success(localResult.first())
                } else {
                    fetchAndSaveRemoteResult()
                }
            }

            is FetchType.Remote -> fetchAndSaveRemoteResult()

            is FetchType.Search -> fetchSearchedSearchRecipeRemote(fetchType.data)
        }
    }

    private suspend fun fetchAndSaveRemoteResult(): Result<RecipeResult> {
        val preference = dataStoreRepository.data.first()
        val query = applyRecipeQuery(preference.selectedMealType, preference.selectedDietType)
        return recipeResultRepository.fetchRecipeResultRemote(query)
            .onSuccess {
                recipeResultRepository.insertRecipeResult(it.toRecipeResultEntity())
            }
    }

    private suspend fun fetchSearchedSearchRecipeRemote(searchString: String): Result<RecipeResult> {
        val query = applySearchRecipeQuery(searchString)
        return recipeResultRepository.fetchSearchedRecipesResult(query)
    }

    private fun applyRecipeQuery(mealType: String, dietType: String): HashMap<String, String> {
        val query: HashMap<String, String> = hashMapOf()
        query[QUERY_NUMBER] = DEFAULT_QUERY_NUMBER
        query[QUERY_API_KEY] = API_KEY
        query[QUERY_TYPE] = mealType
        query[QUERY_DIET] = dietType
        query[QUERY_ADD_RECIPE_INFORMATION] = TRUE
        query[QUERY_FILL_INGREDIENTS] = TRUE
        return query
    }

    private fun applySearchRecipeQuery(searchString: String): HashMap<String, String> {
        val query: HashMap<String, String> = hashMapOf()
        query[Constants.QUERY_SEARCH] = searchString
        query[QUERY_NUMBER] = DEFAULT_QUERY_NUMBER
        query[QUERY_API_KEY] = API_KEY
        query[QUERY_ADD_RECIPE_INFORMATION] = TRUE
        query[QUERY_FILL_INGREDIENTS] = TRUE
        return query
    }
}