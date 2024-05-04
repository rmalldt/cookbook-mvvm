package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.domain.data.Recipe
import kotlinx.coroutines.flow.Flow

interface FavouriteRecipeRepository {

    fun getFavouriteRecipes(): Flow<List<Recipe>>

    suspend fun insertFavouriteRecipe(recipeEntity: RecipeEntity)

    suspend fun deleteFavouriteRecipe(recipeEntity: RecipeEntity)

    suspend fun deleteAllFavouriteRecipes()
}