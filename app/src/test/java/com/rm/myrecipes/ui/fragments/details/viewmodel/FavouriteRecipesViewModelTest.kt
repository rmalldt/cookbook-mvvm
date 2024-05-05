package com.rm.myrecipes.ui.fragments.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.domain.usecase.FavouriteRecipeUseCase
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.favourites.viewmodel.FavouriteRecipesViewModel
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

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
        viewModel = FavouriteRecipesViewModel(mockUseCase)
    }

    @Test
    fun `favouriteRecipesState initial UiState is set to Loading`() = runTest {
        // Given
        coEvery { mockUseCase.invoke() } returns flow {
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
        coEvery { mockUseCase.invoke() } returns flow {
            emit(listOf(provideRecipe()))
        }

        // Act & Assert
        viewModel.fetchFavouriteRecipes()
        viewModel.favouriteRecipesState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(listOf(provideRecipe()))

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { mockUseCase.invoke() }
    }

    @Test
    fun `fetchFavouriteRecipes sets UiState to Error when the local fetch fails`() = runTest {
        //Given
        coEvery { mockUseCase.invoke() } returns flow {
            throw Exception(ERROR_MSG)
        }

        // Act & Assert
        viewModel.fetchFavouriteRecipes()
        viewModel.favouriteRecipesState.test {
            val state = awaitItem()
            state shouldBe UiState.Error<String>(ERROR_MSG)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { mockUseCase.invoke() }
    }

    companion object {
        const val ERROR_MSG = "Something went wrong, please try again later."
    }
}