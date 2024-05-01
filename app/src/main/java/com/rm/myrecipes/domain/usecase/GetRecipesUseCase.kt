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