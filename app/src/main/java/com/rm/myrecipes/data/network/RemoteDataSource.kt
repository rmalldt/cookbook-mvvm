package com.rm.myrecipes.data.network

import com.rm.myrecipes.data.network.dto.RecipesResponse
import retrofit2.Response
import retrofit2.http.QueryMap
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val recipesApi: RecipesApi) {

    suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<RecipesResponse> {
        Timber.tag("Recipe").d("FETCHING FROM NETWORK...")
        return recipesApi.getRecipes(queries)
    }
}