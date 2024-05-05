package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.domain.data.RecipeResult


interface RecipeResultRepository {

    suspend fun loadRecipeResultLocal(): List<RecipeResult>
    suspend fun fetchRecipeResultRemote(query: Map<String, String>): Result<RecipeResult>
    suspend fun fetchSearchedRecipesResult(query: Map<String, String>): Result<RecipeResult>
    suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity)
}