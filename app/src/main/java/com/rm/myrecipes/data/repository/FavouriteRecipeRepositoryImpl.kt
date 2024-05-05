package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeEntity.Companion.toRecipe
import com.rm.myrecipes.data.room.entity.RecipeEntity.Companion.toRecipeEntity
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.repository.FavouriteRecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteRecipeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : FavouriteRecipeRepository{

    override suspend fun getFavouriteRecipes(): Flow<List<Recipe>> = withContext(dispatcher) {
        localDataSource.getRecipes().map { recipeEntityList ->
            recipeEntityList.map { entity ->
                entity.toRecipe()
            }
        }
    }

    override suspend fun insertFavouriteRecipe(recipe: Recipe) = withContext(dispatcher) {
        localDataSource.insertRecipe(recipe.toRecipeEntity())
    }

    override suspend fun deleteFavouriteRecipe(recipe: Recipe) = withContext(dispatcher) {
        localDataSource.deleteRecipe(recipe.toRecipeEntity())
    }

    override suspend fun deleteAllFavouriteRecipes() = withContext(dispatcher) {
        localDataSource.deleteAllRecipes()
    }
}
