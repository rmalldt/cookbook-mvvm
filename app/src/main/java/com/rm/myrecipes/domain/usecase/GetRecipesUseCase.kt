package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.RecipeRepositoryImpl
import com.rm.myrecipes.domain.data.RecipeRepository
import com.rm.myrecipes.domain.data.Recipes
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepositoryImpl
) {

    fun invoke(applied: Boolean): Flow<Recipes> {
        return recipeRepository.getRecipes(applied)
    }
}