package com.rm.myrecipes.data.room

import timber.log.Timber
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    fun loadRecipes(): List<RecipesEntity> {
        Timber.tag("Recipe").d("LOADING FROM DATABASE...")
        return recipesDao.loadRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        Timber.tag("Recipe").d("INSERTING INTO DATABASE...")
        recipesDao.insertRecipes(recipesEntity)
    }
}