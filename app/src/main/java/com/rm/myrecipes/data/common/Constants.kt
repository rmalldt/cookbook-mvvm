package com.rm.myrecipes.data.common

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "1917818a44b1434ba6180c9c1b225853"

        // API Query Keys
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