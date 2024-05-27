package com.rm.myrecipes.data.room

import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    suspend fun getRecipeResult(): List<RecipeResultEntity> {
        Timber.d("RecipesResult: LOADING FROM DATABASE...")
        return recipesDao.getRecipeResult()
    }

    suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity) {
        Timber.d("RecipesResult: INSERTING INTO DATABASE...")
        recipesDao.insertRecipeResult(recipeResultEntity)
    }

    fun getFavouriteRecipes(): Flow<List<RecipeEntity>> {
        Timber.d("Recipes: GETTING FROM DATABASE...")
        return recipesDao.getRecipes()
    }

    suspend fun insertFavouriteRecipe(recipeEntity: RecipeEntity) {
        Timber.d("Recipe: INSERTING INTO DATABASE...")
        return recipesDao.insertRecipe(recipeEntity)
    }

    suspend fun deleteFavouriteRecipe(recipeEntity: RecipeEntity) {
        Timber.d("Recipe: DELETING FROM DATABASE...")
        return recipesDao.deleteRecipe(recipeEntity)
    }

    suspend fun deleteAllFavouriteRecipes() {
        Timber.d("Recipe: DELETING ALL FROM DATABASE...")
        return recipesDao.deleteAllRecipes()
    }
}