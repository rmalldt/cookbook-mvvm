package com.rm.myrecipes.domain.data

import android.os.Parcelable
import com.rm.myrecipes.ui.utils.EMPTY_STRING
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class RecipeResult(val recipes: List<Recipe>)

@Parcelize
data class Recipe(
    val aggregateLikes: Int = 0,
    val cheap: Boolean = false,
    val dairyFree: Boolean = false,
    val extendedIngredients: @RawValue List<ExtendedIngredient>,
    val glutenFree: Boolean = false,
    val recipeId: Int = 0,
    val image: String = EMPTY_STRING,
    val readyInMinutes: Int = 0,
    val sourceName: String? = EMPTY_STRING,
    val sourceUrl: String = EMPTY_STRING,
    val summary: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val vegan: Boolean = false,
    val vegetarian: Boolean = false,
    val veryHealthy: Boolean = false
) : Parcelable

@Parcelize
data class ExtendedIngredient(
    val amount: Double = 0.0,
    val consistency: String = EMPTY_STRING,
    val image: String? = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val original: String = EMPTY_STRING,
    val unit: String = EMPTY_STRING
) : Parcelable