package com.rm.myrecipes.data.di

import com.rm.myrecipes.data.common.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UrlModule {

    @Provides
    @Singleton
    fun provideUrl(): String = ApiConstants.BASE_URL
}