package com.rm.myrecipes.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    // RecipeResult query
    @Query("SELECT * FROM result ORDER BY id ASC")
    suspend fun getRecipeResult(): List<RecipeResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeResult(recipeResultEntity: RecipeResultEntity)

    // Recipe query
    @Query("SELECT * FROM recipe ORDER BY id ASC")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    @Query("DELETE FROM recipe")
    suspend fun deleteAllRecipes()
}