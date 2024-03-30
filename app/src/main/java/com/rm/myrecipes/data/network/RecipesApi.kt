package com.rm.myrecipes.data.network

import com.rm.myrecipes.data.network.dto.RecipesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<RecipesResponse>
}
