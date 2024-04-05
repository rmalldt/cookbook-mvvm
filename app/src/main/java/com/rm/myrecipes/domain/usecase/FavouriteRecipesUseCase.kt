package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.room.entity.RecipeEntity.Companion.toRecipeEntity
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.repository.FavouriteRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteRecipeUseCase @Inject constructor(
    private val favouriteRecipeRepository: FavouriteRecipeRepository
) {

    operator fun invoke(): Flow<List<Recipe>> {
        return favouriteRecipeRepository.getFavouriteRecipes()
    }

    suspend fun insertFavouriteRecipe(recipe: Recipe) {
        favouriteRecipeRepository.insertFavouriteRecipe(recipe.toRecipeEntity())
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        favouriteRecipeRepository.deleteFavouriteRecipe(recipe.toRecipeEntity())
    }

    suspend fun deleteAlRecipes() {
        favouriteRecipeRepository.deleteAllFavouriteRecipes()
    }
}