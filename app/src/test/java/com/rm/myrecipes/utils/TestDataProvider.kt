package com.rm.myrecipes.utils

import com.rm.myrecipes.data.network.dto.ExtendedIngredientDefinition
import com.rm.myrecipes.data.network.dto.RecipeDefinition
import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeResult

// Database Entity
fun provideRecipeResultEntity(): RecipeResultEntity = RecipeResultEntity(
        id = 1,
        listOf(provideRecipe())
    )

fun provideRecipeEntity(): RecipeEntity = RecipeEntity(
        id = 1,
        aggregateLikes = 100,
        cheap = true,
        dairyFree = true,
        extendedIngredients = listOf(provideExtendedIngredient()),
        glutenFree = true,
        image = "img.recipe.com",
        readyInMinutes = 20,
        sourceName = "sourceName",
        sourceUrl = "api.sourceUrl.com",
        summary = "summary/description",
        title = "recipe",
        vegan = true,
        vegetarian = true,
        veryHealthy = true
    )

// Response Models
fun provideRecipeDefinition() = RecipeDefinition(
        aggregateLikes = 100,
        cheap = true,
        dairyFree = true,
        extendedIngredientDefinitions = listOf(provideExtendedIngredientDefinition()),
        glutenFree = true,
        recipeId = 1,
        image = "img.recipe.com",
        readyInMinutes = 20,
        sourceName = "sourceName",
        sourceUrl = "api.sourceUrl.com",
        summary = "summary/description",
        title = "recipe",
        vegan = true,
        vegetarian = true,
        veryHealthy = true
    )

fun provideExtendedIngredientDefinition(): ExtendedIngredientDefinition = ExtendedIngredientDefinition(
        amount = 10.0,
        consistency = "SOLID",
        image = "img.ingredient.com",
        name = "ingredient",
        original = "original",
        unit = "gram"
    )

// Domain Models
fun provideRecipeResult(): RecipeResult = RecipeResult(listOf(provideRecipe()))

fun provideRecipe(): Recipe = Recipe(
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
        summary = "summary/description",
        title = "recipe",
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
