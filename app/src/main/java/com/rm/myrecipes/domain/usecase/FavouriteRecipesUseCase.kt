package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.repository.FavouriteRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteRecipeUseCase @Inject constructor(
    private val favouriteRecipeRepository: FavouriteRecipeRepository
) {

    suspend operator fun invoke(): Flow<List<Recipe>> {
        return favouriteRecipeRepository.getFavouriteRecipes()
    }

    suspend fun insertFavouriteRecipe(recipe: Recipe) {
        favouriteRecipeRepository.insertFavouriteRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        favouriteRecipeRepository.deleteFavouriteRecipe(recipe)
    }

    suspend fun deleteAlRecipes() {
        favouriteRecipeRepository.deleteAllFavouriteRecipes()
    }
}