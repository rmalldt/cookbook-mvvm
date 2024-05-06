package com.rm.myrecipes.domain.data

import android.os.Parcelable
import com.rm.myrecipes.ui.utils.common.EMPTY_STRING
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.UUID

/**
 * State emits only if it detects changes to the value, fetching recipe with same query values
 * doesn't update the stateflow. So, uuid is used to distinguish the result even if the fetching
 * with the same query values for recipe
 */
data class RecipeResult(
    val uuid: String = UUID.randomUUID().toString(),
    val recipes: List<Recipe>
)

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