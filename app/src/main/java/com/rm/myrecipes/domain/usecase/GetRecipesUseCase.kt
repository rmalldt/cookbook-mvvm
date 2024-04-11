package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeResultRepository: RecipeResultRepository,
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(fetchState: FetchState): Flow<RecipeResult> {
        return when (fetchState) {
            is FetchState.FetchLocal -> recipeResultRepository.getRecipeResult(fetchState)
            is FetchState.FetchRemote -> {
                val preference = dataStoreRepository.data.first()
                val query = applyRecipeQuery(preference.selectedMealType, preference.selectedDietType)
                recipeResultRepository.getRecipeResult(fetchState, query)

            }
            is FetchState.FetchSearch -> {
                val query = applySearchRecipeQuery(fetchState.data)
                recipeResultRepository.getRecipeResult(fetchState, query)
            }
        }
    }

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