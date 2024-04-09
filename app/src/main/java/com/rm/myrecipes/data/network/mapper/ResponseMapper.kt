package com.rm.myrecipes.data.network.mapper

import com.rm.myrecipes.data.network.dto.ExtendedIngredientDefinition
import com.rm.myrecipes.data.network.dto.FoodTriviaResponse
import com.rm.myrecipes.data.network.dto.RecipeDefinition
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeResult
import javax.inject.Inject

class ResponseMapper @Inject constructor() {

    fun toRecipeResult(recipesResponse: RecipesResponse): RecipeResult {
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

    fun toFoodTrivia(foodTriviaResponse: FoodTriviaResponse): FoodTrivia {
        foodTriviaResponse.apply {
            return FoodTrivia(trivia)
        }
    }
}
