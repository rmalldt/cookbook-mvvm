package com.rm.myrecipes.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.FoodTriviaResponse
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.utils.FakeApi
import com.rm.myrecipes.utils.MainDispatcherRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

class FoodTriviaRepositoryImplTest {

    // Instantiate fakes
    private val fakeApi = FakeApi()
    private val remoteDataSource: RemoteDataSource = RemoteDataSource(fakeApi)

    private val mapper = ResponseMapper()

    private lateinit var repository: FoodTriviaRepositoryImpl

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = FoodTriviaRepositoryImpl(
            remoteDataSource,
            mapper,
            mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `getFoodTrivia returns FoodTrivia item when remote fetch is successful`() = runTest {
        // Given
        fakeApi.foodTriviaMock = { apikey ->
            val foodTrivia = FoodTriviaResponse("FoodTrivia")
            Response.success(foodTrivia)
        }

        // Act & Assert
        repository.getFoodTrivia("123").test {
            val successData = awaitItem()
            successData.trivia shouldBe "FoodTrivia"

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getFoodTrivia returns error when remote fetch fails`() = runTest {
        // Given
        fakeApi.foodTriviaMock = { apikey ->
            error("API failed")
        }

        // Act and Assert
        repository.getFoodTrivia("123").test {
            val errorData = awaitError()
            errorData.message shouldBe "API failed"

            cancelAndIgnoreRemainingEvents()
        }
    }
}
