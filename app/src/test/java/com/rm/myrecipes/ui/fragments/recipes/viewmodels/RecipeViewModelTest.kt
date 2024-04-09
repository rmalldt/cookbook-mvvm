package com.rm.myrecipes.ui.fragments.recipes.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.domain.usecase.SelectedChipUseCase
import com.rm.myrecipes.ui.common.FetchState
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.NetworkChecker
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

import org.junit.Before
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
    fun `getRecipeResultState is set to Success when network is connected and fetch is successful`() = runTest {
        val item = RecipeResult(listOf(provideRecipe()))
        val fetchState = FetchState.FetchLocal

        every { mockNetworkChecker.hasInternetConnection() } returns true
        every { mockUseCase.invoke(fetchState) } returns flow { emit(item) }
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 100) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker,
            mainDispatcherRule.testDispatcher,
        )

        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(item)

            cancelAndConsumeRemainingEvents()
        }

        verify(exactly = 1) { mockUseCase.invoke(fetchState) }
    }

    @Test
    fun `fetchSafe fetches from local if network is not connected`() = runTest {
        val item = RecipeResult(listOf(provideRecipe()))
        val fetchState = FetchState.FetchLocal

        every { mockNetworkChecker.hasInternetConnection() } returns false
        every { mockUseCase.invoke(fetchState) } returns flow { emit(item) }
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 100) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker,
            mainDispatcherRule.testDispatcher
        )

        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(item)

            cancelAndConsumeRemainingEvents()
        }

        verifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke(fetchState)
        }
    }

    @Test
    fun `fetchSafe fetches from remote if network is connected and the fetchState is FetchRemote`() = runTest {
        val item = RecipeResult(listOf(provideRecipe()))
        val fetchState = FetchState.FetchRemote

        every { mockNetworkChecker.hasInternetConnection() } returns true
        every { mockUseCase.invoke(FetchState.FetchLocal) } returns flow { emit(item) }
        every { mockUseCase.invoke(FetchState.FetchRemote) } returns flow { emit(item) }
        every { mockSelectedChipUseCase.invoke() } returns flow { SelectedChipPreferences(selectedDietId = 100) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker,
            mainDispatcherRule.testDispatcher
        )

        viewModel.fetchSafe(fetchState)
        viewModel.recipeResultState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(item)

            cancelAndConsumeRemainingEvents()
        }

        verifyOrder {
            mockNetworkChecker.hasInternetConnection()
            mockUseCase.invoke(FetchState.FetchLocal)
            mockUseCase.invoke(fetchState)
        }
    }

    @Test
    fun getSelectedChipState() = runTest {
        val item = RecipeResult(listOf(provideRecipe()))
        val preferencesItem = SelectedChipPreferences(selectedDietId = 100)

        every { mockNetworkChecker.hasInternetConnection() } returns true
        every { mockUseCase.invoke(FetchState.FetchLocal) } returns flow { emit(item) }
        every { mockSelectedChipUseCase.invoke() } returns flow { emit(preferencesItem) }

        viewModel = RecipeViewModel(
            mockUseCase,
            mockSelectedChipUseCase,
            mockNetworkChecker,
            mainDispatcherRule.testDispatcher,
        )

        viewModel.selectedChipState.test {
            val state = awaitItem()
            state shouldBe UiState.Success(preferencesItem)

            cancelAndConsumeRemainingEvents()
        }

        verify(exactly = 1) { mockSelectedChipUseCase.invoke() }
    }
}
