package com.rm.myrecipes.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.data.network.RecipesApi
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.RetryInterceptor
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.data.repository.FavouriteRecipeRepositoryImpl
import com.rm.myrecipes.data.repository.FoodTriviaRepositoryImpl
import com.rm.myrecipes.data.repository.RecipeResultRepositoryImpl
import com.rm.myrecipes.data.room.AppDatabase
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.data.room.RecipesDao
import com.rm.myrecipes.domain.repository.FavouriteRecipeRepository
import com.rm.myrecipes.domain.repository.FoodTriviaRepository
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private val Context.dataStore by preferencesDataStore("recipe_datastore")

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor())
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
    fun providesAppDatabase(@ApplicationContext context: Context) : AppDatabase = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun providesRecipesDao(database: AppDatabase): RecipesDao = database.recipesDao()

    @Singleton
    @Provides
    fun provideRecipeRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        dataStoreRepository: DataStoreRepository,
        responseMapper: ResponseMapper
    ): RecipeResultRepository = RecipeResultRepositoryImpl(
            remoteDataSource,
            localDataSource,
            dataStoreRepository,
            responseMapper
        )

    @Singleton
    @Provides
    fun provideFavouriteRecipeRepository(localDataSource: LocalDataSource): FavouriteRecipeRepository =
        FavouriteRecipeRepositoryImpl(localDataSource)

    @Singleton
    @Provides
    fun provideFoodTriviaRepository(remoteDataSource: RemoteDataSource, mapper: ResponseMapper): FoodTriviaRepository =
        FoodTriviaRepositoryImpl(remoteDataSource, mapper)
}