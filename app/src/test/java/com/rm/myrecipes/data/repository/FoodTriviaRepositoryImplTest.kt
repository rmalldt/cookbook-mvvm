package com.rm.myrecipes.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.common.NetworkException
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.dto.FoodTriviaResponse
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.utils.FakeApi
import com.rm.myrecipes.utils.MainDispatcherRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
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
        fakeApi.foodTriviaMock = { _ ->
            val foodTrivia = FoodTriviaResponse("FoodTrivia")
            Response.success(foodTrivia)
        }

        // Act & Assert
        repository.getFoodTrivia("123")
            .onSuccess {
                it.trivia shouldBe "FoodTrivia"
            }
    }

    @Test
    fun `getFoodTrivia returns error when remote fetch fails`() = runTest {
        // Given
        fakeApi.foodTriviaMock = { _ ->
            Response.error(
                501,
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"key\":[\"value\"]}"
                )
            )
        }

        // Act and Assert
        val result = repository.getFoodTrivia("123")
        result.isFailure shouldBe true
        result.onFailure {
            it is NetworkException
        }
    }
}


    /*@Test
    fun `getFoodTrivia returns FoodTrivia item when remote fetch is successful`() = runTest {
        // Given
        fakeApi.foodTriviaMock = { _ ->
            val foodTrivia = FoodTriviaResponse("FoodTrivia")
            Response.success(foodTrivia)
        }

        // Act & Assert
        repository.getFoodTrivia("123") {
            val successData = awaitItem()
            successData.trivia shouldBe "FoodTrivia"

            cancelAndIgnoreRemainingEvents()
        }
    }*/

