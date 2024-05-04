package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.ui.common.FetchState


interface RecipeResultRepository {

    suspend fun getRecipeResult(fetchState: FetchState, query: Map<String,String> = emptyMap()): RecipeResult
}