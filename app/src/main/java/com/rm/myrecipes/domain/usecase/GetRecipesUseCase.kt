package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.domain.data.RecipeRepository
import com.rm.myrecipes.domain.data.Recipes
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    fun invoke(fetchState: FetchState): Flow<Recipes> {
        return recipeRepository.getRecipes(fetchState)
    }
}