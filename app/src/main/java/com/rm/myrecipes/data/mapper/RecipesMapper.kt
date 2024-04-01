package com.rm.myrecipes.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rm.myrecipes.data.network.dto.ExtendedIngredientDefinition
import com.rm.myrecipes.data.network.dto.RecipeDefinition
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.Recipes
import javax.inject.Inject

class RecipesMapper @Inject constructor() {

    private var gson = Gson()

    fun recipesToJson(recipes: Recipes): String = gson.toJson(recipes)

    fun jsonToRecipes(jsonString: String): Recipes {
        val recipesType = object : TypeToken<Recipes>() {}.type
        return gson.fromJson(jsonString, recipesType)
    }


}