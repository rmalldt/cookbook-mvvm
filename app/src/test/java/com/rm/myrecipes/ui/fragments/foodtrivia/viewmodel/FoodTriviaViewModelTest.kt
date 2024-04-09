package com.rm.myrecipes.ui.fragments.foodtrivia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.usecase.FoodTriviaUseCase
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.NetworkChecker
import com.rm.myrecipes.utils.MainDispatcherRule
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        viewModel = FoodTriviaViewModel(mockUseCase, mockNetworkChecker, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `getFoodTriviaState is set to Success when network is connected`() = runTest {
        // Given
        val item = FoodTrivia("FoodTrivia")
        every { mockNetworkChecker.hasInternetConnection() } returns true
        every { mockUseCase.invoke() } returns flow { emit(item) }


        // Act & Assert
        viewModel.safeCall()
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(item)
        }

        verifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke()
        }
    }

    @Test
    fun `getFoodTriviaState is set to Success when network is not connected`() = runTest {
        // Given
        val item = FoodTrivia("FoodTrivia")
        every { mockNetworkChecker.hasInternetConnection() } returns false
        every { mockUseCase.invoke() } returns flow { emit(item) }

        // Act & Assert
        viewModel.safeCall()
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Error("No network connection")
        }

        verify(exactly = 1) { mockNetworkChecker.hasInternetConnection() }
    }

    @Test
    fun `getFoodTriviaState is set to Error when fetch fails`() = runTest {
        // Given
        every { mockNetworkChecker.hasInternetConnection() } returns true
        every { mockUseCase.invoke() } returns flow {
            error("Something went wrong")
        }

        // Act & Assert
        viewModel.safeCall()
        viewModel.foodTriviaState.test {
            val state = awaitItem()
            state shouldBe UiState.Error("Something went wrong")
        }

        verifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke()
        }
    }

}