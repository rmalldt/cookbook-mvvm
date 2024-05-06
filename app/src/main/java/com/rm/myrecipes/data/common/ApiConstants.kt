package com.rm.myrecipes.data.common

class ApiConstants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com/"
        const val INGREDIENT_IMAGE_URL = "https://img.spoonacular.com/ingredients_100x100/"
        const val API_KEY = "apiKey"

        // API Query Keys
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Recipe Default Meal and Diet types
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_QUERY_NUMBER = "20"
        const val TRUE = "true"
        const val FALSE = "false"
    }
}
