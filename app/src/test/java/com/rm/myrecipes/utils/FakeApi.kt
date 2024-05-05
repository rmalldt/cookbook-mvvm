package com.rm.myrecipes.utils

import com.rm.myrecipes.data.network.RecipesApi
import com.rm.myrecipes.data.network.dto.FoodTriviaResponse
import com.rm.myrecipes.data.network.dto.RecipesResponse
import retrofit2.Response

class FakeApi : RecipesApi {

    // Default behaviour is to return empty list
    var recipeResponseMock: suspend (query: Map<String, String>) -> Response<RecipesResponse> = { _ ->
        val recipesResponse = RecipesResponse(emptyList())
        Response.success(recipesResponse)
    }

    override suspend fun getRecipesResponse(queries: Map<String, String>): Response<RecipesResponse> {
        return recipeResponseMock(queries)
    }

    // Default behaviour to return FoodTriviaResponse
    var foodTriviaMock: suspend (apikey: String) -> Response<FoodTriviaResponse> = {
        val foodTrivia = FoodTriviaResponse("FoodTrivia")
        Response.success(foodTrivia)
    }

    override suspend fun getFoodTrivia(apiKey: String): Response<FoodTriviaResponse> {
        return foodTriviaMock(apiKey)
    }
}