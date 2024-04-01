package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.domain.data.RecipeRepository
import com.rm.myrecipes.domain.data.Recipes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    fun invoke(queries: Map<String, String>): Flow<Recipes> {
        return recipeRepository.getRecipes(queries)
    }
}