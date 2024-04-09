package com.rm.myrecipes.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.ui.common.FetchState
import com.rm.myrecipes.utils.FakeApi
import com.rm.myrecipes.utils.FakeDatabase
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import com.rm.myrecipes.utils.provideRecipeDefinition
import com.rm.myrecipes.utils.provideRecipeResult
import com.rm.myrecipes.utils.provideRecipeResultEntity
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

class RecipeResultRepositoryImplTest {

    // Instantiate fakes
    private val fakeApi = FakeApi()
    private val remoteDataSource: RemoteDataSource = RemoteDataSource(fakeApi)
    private val fakeDatabase = FakeDatabase()
    private val localDataSource = LocalDataSource(fakeDatabase)

    private val mockDataStoreRepository: DataStoreRepository = mockk()
    private val mapper = ResponseMapper()

    private lateinit var recipeResultRepositoryImpl: RecipeResultRepositoryImpl

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule: TestRule = MainDispatcherRule()

    @Before
    fun setUp() {
        recipeResultRepositoryImpl = RecipeResultRepositoryImpl(
            remoteDataSource,
            localDataSource,
            mockDataStoreRepository,
            mapper
        )
    }

    @Test
    fun `getRecipeResul fetches from local when FetchState is FetchLocal and local is not empty`() = runTest {
        // Given
        val fetchState = FetchState.FetchLocal

        fakeDatabase.recipeResultEntityMock = {
            listOf(provideRecipeResultEntity())
        }

        // Act & Assert
        recipeResultRepositoryImpl.getRecipeResult(fetchState).test {
            val localData = awaitItem()
            localData.recipes.size shouldBe 1
            localData.recipes[0].recipeId shouldBe 1

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getRecipeResult fetches from remote when FetchState is FetchLocal and local is empty and stores locally`()
    = runTest {
        // Given
        val fetchState = FetchState.FetchLocal

        coEvery { mockDataStoreRepository.data } returns flow {
            emit(SelectedChipPreferences(selectedDietId = 100))
        }

        fakeDatabase.recipeResultEntityMock = {
            emptyList()
        }

        fakeApi.recipeResponseMock = { query ->
            val recipesResponse = RecipesResponse(listOf(provideRecipeDefinition()))
            Response.success(recipesResponse)
        }

        // Act & assert
        recipeResultRepositoryImpl.getRecipeResult(fetchState).test {
            val remoteData = awaitItem()
            remoteData.recipes.size shouldBe 1
            remoteData.recipes[0].recipeId shouldBe  1

            fakeDatabase.insertedRecipeResultEntity shouldBe true

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getRecipeResult fetches from remote when FetchState is FetchRemote and stores locally`() = runTest {
        // Given
        val fetchState = FetchState.FetchRemote

        coEvery { mockDataStoreRepository.data } returns flow {
            emit(SelectedChipPreferences(selectedDietId = 100))
        }

        fakeApi.recipeResponseMock = { query ->
            val recipesResponse = RecipesResponse(listOf(provideRecipeDefinition()))
            Response.success(recipesResponse)
        }

        // Act & assert
        recipeResultRepositoryImpl.getRecipeResult(fetchState).test {
            val remoteData = awaitItem()
            remoteData.recipes.size shouldBe 1
            remoteData.recipes[0].recipeId shouldBe  1

            fakeDatabase.insertedRecipeResultEntity shouldBe true

            cancelAndConsumeRemainingEvents()
        }

        assertEquals(true, fakeDatabase.insertedRecipeResultEntity)
    }

    @Test
    fun `getRecipeResult fetches from remote when FetchState is FetchSearch`() = runTest {
        // Given
        val fetchState = FetchState.FetchSearch

        coEvery { mockDataStoreRepository.data } returns flow {
            emit(SelectedChipPreferences(selectedDietId = 100))
        }

        fakeApi.recipeResponseMock = { query ->
            val recipesResponse = RecipesResponse(listOf(provideRecipeDefinition()))
            Response.success(recipesResponse)
        }

        // Act & assert
        recipeResultRepositoryImpl.getRecipeResult(fetchState).test {
            val remoteData = awaitItem()
            remoteData.recipes.size shouldBe 1
            remoteData.recipes[0].recipeId shouldBe  1

            fakeDatabase.insertedRecipeResultEntity shouldBe false

            cancelAndConsumeRemainingEvents()
        }
    }
}
