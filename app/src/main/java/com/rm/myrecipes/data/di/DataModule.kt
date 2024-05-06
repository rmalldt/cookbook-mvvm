package com.rm.myrecipes.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rm.myrecipes.data.common.ApiConstants
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
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(retryInterceptor: RetryInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS )
            .build()
    }


    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        url: String
    ): Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi = retrofit.create(RecipesApi::class.java)

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesRecipesDao(database: AppDatabase): RecipesDao = database.recipesDao()


    private val Context.dataStore by preferencesDataStore("recipe_datastore")

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore



    @Provides
    @Singleton
    fun provideRecipeRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        responseMapper: ResponseMapper,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): RecipeResultRepository = RecipeResultRepositoryImpl(
        remoteDataSource,
        localDataSource,
        responseMapper,
        dispatcher
    )

    @Provides
    @Singleton
    fun provideFavouriteRecipeRepository(
        localDataSource: LocalDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): FavouriteRecipeRepository = FavouriteRecipeRepositoryImpl(localDataSource, dispatcher)

    @Provides
    @Singleton
    fun provideFoodTriviaRepository(
        remoteDataSource: RemoteDataSource,
        mapper: ResponseMapper,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): FoodTriviaRepository = FoodTriviaRepositoryImpl(remoteDataSource, mapper, dispatcher)
}
