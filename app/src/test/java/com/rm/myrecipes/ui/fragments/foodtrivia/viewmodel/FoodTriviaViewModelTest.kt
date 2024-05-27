package com.rm.myrecipes.ui.fragments.foodtrivia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.usecase.FoodTriviaUseCase
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.network.NetworkChecker
import com.rm.myrecipes.utils.MainDispatcherRule
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class FoodTriviaViewModelTest {

    private val mockUseCase: FoodTriviaUseCase = mockk()
    private val mockNetworkChecker: NetworkChecker = mockk()

    private lateinit var viewModel: FoodTriviaViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = FoodTriviaViewModel(mockUseCase, mockNetworkChecker)
    }

    @Test
    fun `getFoodTriviaState initial UiState is set to Loading`() = runTest {
        // Given
        val item = FoodTrivia("FoodTrivia")
        every { mockNetworkChecker.hasInternetConnection() } returns true
        coEvery { mockUseCase.invoke() } returns Result.success(item)

        // Act & Assert
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Loading
        }
    }

    @Test
    fun `getFoodTriviaState is set to Success when network is connected`() = runTest {
        // Given
        val item = FoodTrivia("FoodTrivia")
        every { mockNetworkChecker.hasInternetConnection() } returns true
        coEvery { mockUseCase.invoke() } returns Result.success(item)

        // Act & Assert
        viewModel.safeCall()
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(item)

            cancelAndConsumeRemainingEvents()
        }

        coVerifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke()
        }
    }

    @Test
    fun `getFoodTriviaState is set to Error when network is not connected`() = runTest {
        // Given
        every { mockNetworkChecker.hasInternetConnection() } returns false

        // Act & Assert
        viewModel.safeCall()
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Error(ERR_NO_INTERNET)
        }

        verify(exactly = 1) { mockNetworkChecker.hasInternetConnection() }
    }

    @Test
    fun `getFoodTriviaState is set to Error when fetch fails`() = runTest {
        // Given
        every { mockNetworkChecker.hasInternetConnection() } returns true
        coEvery { mockUseCase.invoke() } returns Result.failure(Exception())

        // Act & Assert
        viewModel.safeCall()
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Error(ERR_MSG)
        }

        coVerifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke()
        }
    }

    companion object {
        const val ERR_NO_INTERNET = "No network connection."
        const val ERR_MSG = "Something went wrong, please try again."
    }
}
