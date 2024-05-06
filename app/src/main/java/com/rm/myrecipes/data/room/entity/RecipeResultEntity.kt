package com.rm.myrecipes.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeResult

@Entity(tableName = "result")
data class RecipeResultEntity(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    val recipes: List<Recipe>
) {
    companion object {

        fun RecipeResultEntity.toRecipeResult(): RecipeResult = RecipeResult(recipes = recipes)

        fun RecipeResult.toRecipeResultEntity(): RecipeResultEntity = RecipeResultEntity(recipes = recipes)
    }
}
