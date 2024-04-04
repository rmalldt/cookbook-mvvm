package com.rm.myrecipes.data.network.dto

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("results") val recipes: List<RecipeDefinition>
)

data class RecipeDefinition(
    @SerializedName("aggregateLikes") val aggregateLikes: Int,
    @SerializedName("cheap") val cheap: Boolean,
    @SerializedName("dairyFree") val dairyFree: Boolean,
    @SerializedName("extendedIngredients") val extendedIngredientDefinitions: List<ExtendedIngredientDefinition>,
    @SerializedName("glutenFree") val glutenFree: Boolean,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("readyInMinutes") val readyInMinutes: Int,
    @SerializedName("sourceName") val sourceName: String?,
    @SerializedName("sourceUrl") val sourceUrl: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("title") val title: String,
    @SerializedName("vegan") val vegan: Boolean,
    @SerializedName("vegetarian") val vegetarian: Boolean,
    @SerializedName("veryHealthy") val veryHealthy: Boolean
)

data class ExtendedIngredientDefinition(
    @SerializedName("amount") val amount: Double,
    @SerializedName("consistency") val consistency: String,
    @SerializedName("image") val image: String?,
    @SerializedName("name") val name: String,
    @SerializedName("original") val original: String,
    @SerializedName("unit") val unit: String
)