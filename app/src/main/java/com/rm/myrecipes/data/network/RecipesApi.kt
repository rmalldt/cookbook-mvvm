package com.rm.myrecipes.data.network

import com.rm.myrecipes.data.network.dto.FoodTriviaResponse
import com.rm.myrecipes.data.network.dto.RecipesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipesResponse(@QueryMap queries: Map<String, String>): Response<RecipesResponse>

    @GET("/food/trivia/random")
    suspend fun getFoodTrivia(@Query("apiKey") apiKey: String): Response<FoodTriviaResponse>
}
