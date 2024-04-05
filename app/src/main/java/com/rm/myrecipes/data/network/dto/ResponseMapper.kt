package com.rm.myrecipes.data.network.dto

import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeResult
import javax.inject.Inject

class RecipeResponseMapper @Inject constructor() {

    fun mapToRecipesResultEntity(recipesResponse: RecipesResponse) : RecipeResultEntity =
        RecipeResultEntity(recipes = mapToRecipeResult(recipesResponse).recipes)

    fun mapToRecipeResult(recipesResponse: RecipesResponse): RecipeResult {
        return RecipeResult(recipesResponse.recipes.map { toRecipe(it) })
    }

    private fun toRecipe(recipeDefinition: RecipeDefinition): Recipe {
        recipeDefinition.apply {
            return Recipe(
                aggregateLikes,
                cheap,
                dairyFree,
                extendedIngredientDefinitions.map(::toExtendedIngredient),
                glutenFree,
                recipeId,
                image,
                readyInMinutes,
                sourceName,
                sourceUrl,
                summary,
                title,
                vegan,
                vegetarian,
                veryHealthy
            )
        }
    }

    private fun toExtendedIngredient(extendedIngredient: ExtendedIngredientDefinition): ExtendedIngredient {
        extendedIngredient.apply {
            return ExtendedIngredient(
                amount,
                consistency,
                image,
                name,
                original,
                unit
            )
        }
    }
}