package com.rm.myrecipes.data.network

import com.rm.myrecipes.data.network.dto.FoodTriviaResponse
import com.rm.myrecipes.data.network.dto.RecipesResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val recipesApi: RecipesApi) {

    suspend fun getRecipesResponse(queries: Map<String, String>): Response<RecipesResponse> {
        Timber.d("RecipesResult: FETCHING FROM NETWORK...")

        val res = recipesApi.getRecipesResponse(queries)
        Timber.d("RecipesResult: ${res.body()}")
        return res
    }

    suspend fun getFoodTrivia(apiKey: String): Response<FoodTriviaResponse> {
        Timber.d("RecipeTrivia: FETCHING FROM NETWORK...")
        return recipesApi.getFoodTrivia(apiKey)
    }
}
