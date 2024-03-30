package com.rm.myrecipes.domain.data

import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.data.room.RecipesEntity
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    fun getRecipes(queries: Map<String, String>): Flow<RecipesResponse>

    fun loadRecipes(): Flow<List<RecipesEntity>>

    suspend fun insertRecipes(recipesEntity: RecipesEntity)

}