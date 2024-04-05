package com.rm.myrecipes.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "aggregate_likes") val aggregateLikes: Int,
    @ColumnInfo(name = "cheap") val cheap: Boolean,
    @ColumnInfo(name = "dairy_free") val dairyFree: Boolean,
    @ColumnInfo(name = "extended_ingredients") val extendedIngredients: List<ExtendedIngredient>,
    @ColumnInfo(name = "gluten_free") val glutenFree: Boolean,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "ready_in_minutes") val readyInMinutes: Int,
    @ColumnInfo(name = "source_name") val sourceName: String?,
    @ColumnInfo(name = "source_url") val sourceUrl: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vegan") val vegan: Boolean,
    @ColumnInfo(name = "vegetarian") val vegetarian: Boolean,
    @ColumnInfo(name = "very_healthy") val veryHealthy: Boolean
) {
    companion object {

        fun RecipeEntity.toRecipe() = Recipe(
            aggregateLikes = this.aggregateLikes,
            cheap = cheap,
            dairyFree =  dairyFree,
            extendedIngredients = extendedIngredients,
            glutenFree = glutenFree,
            recipeId = id,
            image = image,
            readyInMinutes = readyInMinutes,
            sourceName = sourceName,
            sourceUrl = sourceUrl,
            summary = summary,
            title = title,
            vegan = vegan,
            vegetarian = vegetarian,
            veryHealthy = veryHealthy
        )

        fun Recipe.toRecipeEntity() = RecipeEntity(
            recipeId,
            aggregateLikes = this.aggregateLikes,
            cheap = cheap,
            dairyFree =  dairyFree,
            extendedIngredients = extendedIngredients,
            glutenFree = glutenFree,
            image = image,
            readyInMinutes = readyInMinutes,
            sourceName = sourceName,
            sourceUrl = sourceUrl,
            summary = summary,
            title = title,
            vegan = vegan,
            vegetarian = vegetarian,
            veryHealthy = veryHealthy
        )
    }
}
