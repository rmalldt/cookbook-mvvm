package com.rm.myrecipes.data.di

import android.content.Context
import com.rm.myrecipes.data.room.AppDatabase
import com.rm.myrecipes.data.room.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context) : AppDatabase = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun providesRecipesDao(database: AppDatabase): RecipesDao = database.recipesDao()
}