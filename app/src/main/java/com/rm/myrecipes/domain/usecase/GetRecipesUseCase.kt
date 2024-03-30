package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.network.dto.ExtendedIngredientDefinition
import com.rm.myrecipes.data.network.dto.RecipeDefinition
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    fun invoke(queries: Map<String, String>): Flow<List<Recipe>> {
        return recipeRepository.getRecipes(queries).map(::mapResponseToRecipes)
    }

    private fun mapResponseToRecipes(recipesResponse: RecipesResponse): List<Recipe> =
        recipesResponse.recipes.map(::toRecipe)

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