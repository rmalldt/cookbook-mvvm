package com.rm.myrecipes.domain.data

import com.rm.myrecipes.data.dto.RecipesResponse
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    fun getRecipes(queries: Map<String, String>): Flow<RecipesResponse>
}