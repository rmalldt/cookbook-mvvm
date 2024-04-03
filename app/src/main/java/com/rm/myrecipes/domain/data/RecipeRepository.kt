package com.rm.myrecipes.domain.data

import com.rm.myrecipes.data.BaseRepository
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow


interface RecipeRepository : BaseRepository {

    fun getRecipes(fetchState: FetchState): Flow<Recipes>
}