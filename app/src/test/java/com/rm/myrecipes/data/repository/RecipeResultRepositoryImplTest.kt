package com.rm.myrecipes.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.ui.common.FetchType
import com.rm.myrecipes.utils.FakeApi
import com.rm.myrecipes.utils.FakeDatabase
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import com.rm.myrecipes.utils.provideRecipeDefinition
import com.rm.myrecipes.utils.provideRecipeEntity
import com.rm.myrecipes.utils.provideRecipeResultEntity
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class RecipeResultRepositoryImplTest {

    // Instantiate fakes
    private val fakeApi = FakeApi()
    private val remoteDataSource: RemoteDataSource = RemoteDataSource(fakeApi)
    private val fakeDatabase = FakeDatabase()
    private val localDataSource = LocalDataSource(fakeDatabase)

    private val mapper = ResponseMapper()

    private lateinit var recipeResultRepositoryImpl: RecipeResultRepositoryImpl

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        recipeResultRepositoryImpl = RecipeResultRepositoryImpl(
            remoteDataSource,
            localDataSource,
            mapper,
            mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `loadRecipeResultLocal loads from local`() = runTest {
        // Given
        fakeDatabase.recipeResultEntityMock = { listOf(provideRecipeResultEntity()) }

        // Act & Assert
        val localData = recipeResultRepositoryImpl.loadRecipeResultLocal()
        localData.size shouldBe 1
        localData[0].recipes[0].recipeId shouldBe 1
    }

    @Test
    fun `insertRecipeResult insert into local`() = runTest {
        // Given
        val recipeEntity = provideRecipeResultEntity()

        // Act & Assert
        recipeResultRepositoryImpl.insertRecipeResult(recipeEntity)
        fakeDatabase.insertedRecipeResultEntity shouldBe true
    }


    @Test
    fun `fetchRecipeResultRemote fetches from remote`() = runTest {
        // Given
        val queryMap = mockk<Map<String, String>>()

        fakeApi.recipeResponseMock = { _ ->
            val recipesResponse = RecipesResponse(listOf(provideRecipeDefinition()))
            Response.success(recipesResponse)
        }

        // Act & assert
        val result = recipeResultRepositoryImpl.fetchRecipeResultRemote(queryMap)
        result.onSuccess {
            it.recipes.size shouldBe 1
            it.recipes[0].recipeId shouldBe 1
        }
    }

    @Test
    fun `fetchSearchedRecipesResult fetches from remote`() = runTest {
        // Given
        val queryMap = mockk<Map<String, String>>()

        fakeApi.recipeResponseMock = { _ ->
            val recipesResponse = RecipesResponse(listOf(provideRecipeDefinition()))
            Response.success(recipesResponse)
        }

        // Act & assert
        val result = recipeResultRepositoryImpl.fetchSearchedRecipesResult(queryMap)
        result.onSuccess {
            it.recipes.size shouldBe 1
            it.recipes[0].recipeId shouldBe 1
        }
    }
}
