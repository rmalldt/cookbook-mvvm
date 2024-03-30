package com.rm.myrecipes.data.di

import com.rm.myrecipes.data.RecipeRepositoryImpl
import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.data.network.RecipesApi
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.domain.data.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS )
            .build()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi = retrofit.create(RecipesApi::class.java)

    @Singleton
    @Provides
    fun provideRecipeRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): RecipeRepository =
        RecipeRepositoryImpl(remoteDataSource, localDataSource)
}