package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.domain.data.Recipe
import kotlinx.coroutines.flow.Flow

interface FavouriteRecipeRepository {

    suspend fun getFavouriteRecipes(): Flow<List<Recipe>>
    suspend fun insertFavouriteRecipe(recipe: Recipe)
    suspend fun deleteFavouriteRecipe(recipe: Recipe)
    suspend fun deleteAllFavouriteRecipes()
}