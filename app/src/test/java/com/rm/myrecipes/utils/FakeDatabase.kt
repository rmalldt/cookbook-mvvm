package com.rm.myrecipes.utils

import com.rm.myrecipes.data.room.RecipesDao
import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDatabase : RecipesDao {

    var insertedRecipeEntity = false
    var insertedRecipeResultEntity = false
    var deletedRecipeEntity = false
    var deletedAll = false


    // Default behaviour
    var recipeResultEntityMock: suspend () -> List<RecipeResultEntity> = {
        emptyList()
    }

    override suspend fun getRecipeResult(): List<RecipeResultEntity> {
        return recipeResultEntityMock()
    }

    override suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity) {
        insertedRecipeResultEntity = true
    }

    override fun getRecipes(): Flow<List<RecipeEntity>> = flow {
        emit(listOf(provideRecipeEntity()))
    }

    override suspend fun insertRecipe(recipeEntity: RecipeEntity) {
        insertedRecipeEntity = true
    }

    override suspend fun deleteRecipe(recipeEntity: RecipeEntity) {
        deletedRecipeEntity = true
    }

    override suspend fun deleteAllRecipes() {
        deletedAll = true
    }
}