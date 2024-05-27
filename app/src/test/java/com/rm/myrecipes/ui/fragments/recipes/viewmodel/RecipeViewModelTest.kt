package com.rm.myrecipes.ui.fragments.recipes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.domain.usecase.SelectedChipUseCase
import com.rm.myrecipes.ui.common.FetchType
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.network.NetworkChecker
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import com.rm.myrecipes.utils.provideRecipeResult
import io.kotest.matchers.shouldBe
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RecipeViewModelTest {

    private val mockUseCase: GetRecipesUseCase = mockk()
    private val mockSelectedChipUseCase: SelectedChipUseCase = mockk()
    private val mockNetworkChecker: NetworkChecker = mockk()

    private lateinit var viewModel: RecipeViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `recipeResultState initial UiState is set to Loading`() = runTest {
        // Given
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 10) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker
        )

        // Act & Assert
        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Loading
        }
    }

    @Test
    fun `fetchSafe fetches from local if network is not connected`() = runTest {
        val localItem = provideRecipeResult()
        val fetchRemote = FetchType.Remote
        val fetchLocal = FetchType.Local

        every { mockNetworkChecker.hasInternetConnection() } returns false
        coEvery { mockUseCase.invoke(fetchLocal) } returns Result.success(localItem)
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 10) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker
        )

        viewModel.fetchSafe(fetchRemote)
        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(localItem)

            cancelAndConsumeRemainingEvents()
        }

        coVerify(Ordering.SEQUENCE) {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke(fetchLocal)
        }
    }

    @Test
    fun `fetchSafe fetches from remote if network is connected and the fetchState is FetchRemote`() = runTest {
        val item = RecipeResult("123", listOf(provideRecipe()))
        val fetchRemote = FetchType.Remote

        every { mockNetworkChecker.hasInternetConnection() } returns true
        coEvery { mockUseCase.invoke(fetchRemote) } returns Result.success(item)
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 10) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker
        )

        viewModel.fetchSafe(fetchRemote)
        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(item)

            cancelAndConsumeRemainingEvents()
        }

        coVerify(Ordering.SEQUENCE) {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke(fetchRemote)
        }
    }

    @Test
    fun `fetchRecipe sets recipeResultState to Error when fetch fails`() = runTest {
        // Given
        val fetchType = FetchType.Remote
        every { mockNetworkChecker.hasInternetConnection() } returns true
        coEvery { mockUseCase.invoke(fetchType) } returns Result.failure(Exception())
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 10) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker
        )

        // Act & Assert
        viewModel.fetchSafe(fetchType)
        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Error(ERROR_MSG)
        }

        coVerifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke(fetchType)
        }
    }


    @Test
    fun getSelectedChipState() = runTest {
        val item = RecipeResult("123", listOf(provideRecipe()))
        val preferencesItem = SelectedChipPreferences(selectedDietId = 100)

        every { mockNetworkChecker.hasInternetConnection() } returns true
        coEvery { mockUseCase.invoke(FetchType.Local) } returns Result.success(item)
        every { mockSelectedChipUseCase.invoke() } returns flow { emit(preferencesItem) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker
        )

        viewModel.selectedChipState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(preferencesItem)

            cancelAndConsumeRemainingEvents()
        }

        verify(exactly = 1) { mockSelectedChipUseCase.invoke() }
    }

    companion object {
        const val ERROR_MSG = "Something went wrong. Please check your internet connection."
    }
}
