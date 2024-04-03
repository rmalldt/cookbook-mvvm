package com.rm.myrecipes.domain.data

import com.rm.myrecipes.data.BaseRepository
import kotlinx.coroutines.flow.Flow


interface RecipeRepository : BaseRepository {

    fun getRecipes(applied: Boolean): Flow<Recipes>
}