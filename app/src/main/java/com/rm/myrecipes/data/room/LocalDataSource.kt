package com.rm.myrecipes.data.room

import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    fun loadRecipes(): Flow<List<RecipesEntity>> {
        Timber.tag("Recipe").d("LOADING FROM DATABASE...")
        return recipesDao.loadRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        Timber.tag("Recipe").d("INSERTING INTO DATABASE...")
        recipesDao.insertRecipes(recipesEntity)
    }
}