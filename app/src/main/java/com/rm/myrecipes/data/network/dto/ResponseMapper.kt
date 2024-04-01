package com.rm.myrecipes.data.network.dto

import com.rm.myrecipes.data.room.RecipesEntity
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.Recipes
import javax.inject.Inject

class RecipeResponseMapper @Inject constructor() {

    fun mapToRecipesEntity(recipesResponse: RecipesResponse) : RecipesEntity =
        RecipesEntity(recipes = mapToRecipes(recipesResponse).recipes)

    fun mapToRecipes(recipesResponse: RecipesResponse): Recipes {
        return Recipes(recipesResponse.recipes.map { toRecipe(it) })
    }

    private fun toRecipe(recipeDefinition: RecipeDefinition): Recipe {
        recipeDefinition.apply {
            return Recipe(
                aggregateLikes,
                cheap,
                dairyFree,
                extendedIngredientDefinitions.map(::toExtendedIngredient),
                glutenFree,
                id,
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