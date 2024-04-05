package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.data.BaseRepository
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow


interface RecipeResultRepository : BaseRepository {

    fun getRecipeResult(fetchState: FetchState): Flow<RecipeResult>
}