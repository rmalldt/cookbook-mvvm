package com.rm.myrecipes.ui.fragments.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.domain.usecase.FavouriteRecipeUseCase
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavouriteRecipesViewModelTest {

    // Instantiate fakes
    private val mockUseCase: FavouriteRecipeUseCase = mockk()

    private lateinit var viewModel: FavouriteRecipesViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = FavouriteRecipesViewModel(mockUseCase, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `favouriteRecipesState initial UiState is set to Loading`() = runTest {
        // Given
        every { mockUseCase.invoke() } returns flow {
            emit(listOf(provideRecipe()))
        }

        // Act & Assert
        viewModel.favouriteRecipesState.test {
            val state = awaitItem()
            state shouldBe UiState.Loading
        }
    }

    @Test
    fun `fetchFavouriteRecipes sets UiState to Success when the local fetch is successful `() = runTest {
        // Given
        every { mockUseCase.invoke() } returns flow {
            emit(listOf(provideRecipe()))
        }

        // Act & Assert
        viewModel.fetchFavouriteRecipes()
        viewModel.favouriteRecipesState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(listOf(provideRecipe()))

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { mockUseCase.invoke() }
    }

    @Test
    fun `fetchFavouriteRecipes sets UiState to Error when the local fetch fails `() = runTest {
        //Given
        every { mockUseCase.invoke() } returns flow {
            error("Something went wrong")
        }

        // Act & Assert
        viewModel.fetchFavouriteRecipes()
        viewModel.favouriteRecipesState.test {
            val state = awaitItem()
            state shouldBe UiState.Error<String>("Something went wrong")

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { mockUseCase.invoke() }
    }
}