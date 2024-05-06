package com.rm.myrecipes

import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe

object TestData {

    fun provideRecipeList() = listOf(recipe1, recipe2)

    val recipe1: Recipe = Recipe(
        aggregateLikes = 100,
        cheap = true,
        dairyFree = true,
        extendedIngredients = listOf(provideExtendedIngredient()),
        glutenFree = true,
        recipeId = 1,
        image = "img.recipe.com",
        readyInMinutes = 20,
        sourceName = "sourceName",
        sourceUrl = "api.sourceUrl.com",
        summary = "Recipe One Description",
        title = "Recipe One",
        vegan = true,
        vegetarian = true,
        veryHealthy = true
    )

    val recipe2: Recipe = Recipe(
        aggregateLikes = 100,
        cheap = true,
        dairyFree = true,
        extendedIngredients = listOf(provideExtendedIngredient()),
        glutenFree = true,
        recipeId = 1,
        image = "img.recipe.com",
        readyInMinutes = 20,
        sourceName = "sourceName",
        sourceUrl = "api.sourceUrl.com",
        summary = "Recipe Two Description",
        title = "Recipe Two",
        vegan = true,
        vegetarian = true,
        veryHealthy = true
    )

    fun provideExtendedIngredient(): ExtendedIngredient = ExtendedIngredient(
        amount = 10.0,
        consistency = "SOLID",
        image = "img.ingredient.com",
        name = "ingredient",
        original = "original",
        unit = "gram"
    )
}