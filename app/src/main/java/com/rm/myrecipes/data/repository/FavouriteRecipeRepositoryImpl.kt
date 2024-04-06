package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeEntity.Companion.toRecipe
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.repository.FavouriteRecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteRecipeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : FavouriteRecipeRepository{

    override fun getFavouriteRecipes(): Flow<List<Recipe>> {
        return localDataSource.getRecipes().map { entityList ->
            entityList.map {
                it.toRecipe()
            }
        }
    }

    override suspend fun insertFavouriteRecipe(recipeEntity: RecipeEntity) {
        localDataSource.insertRecipe(recipeEntity)
    }

    override suspend fun deleteFavouriteRecipe(recipeEntity: RecipeEntity) {
        localDataSource.deleteRecipe(recipeEntity)
    }

    override suspend fun deleteAllFavouriteRecipes() {
        localDataSource.deleteAllRecipes()
    }
}