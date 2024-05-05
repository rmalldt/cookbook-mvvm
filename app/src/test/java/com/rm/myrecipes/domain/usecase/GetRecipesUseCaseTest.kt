package com.rm.myrecipes.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.data.room.entity.RecipeResultEntity.Companion.toRecipeResultEntity
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import com.rm.myrecipes.ui.common.FetchType
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipeResult
import io.kotest.matchers.shouldBe
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GetRecipesUseCaseTest() {

    // Instantiate fakes
    private val dataStore: DataStoreRepository = mockk()
    private var repository: RecipeResultRepository = mockk()
    private lateinit var useCase: GetRecipesUseCase

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        useCase = GetRecipesUseCase(repository, dataStore)
    }

    @Test
    fun `invoke fetches from local when fetch type is local`() = runTest {
        // Given
        val fetchType = FetchType.Local

        val result = listOf(provideRecipeResult())
        coEvery { repository.loadRecipeResultLocal() } returns result

        useCase.invoke(fetchType)
            .onSuccess {
                it.recipes.size shouldBe result[0].recipes.size
                it.recipes[0].recipeId shouldBe result[0].recipes[0].recipeId
            }
    }

    @Test
    fun `invoke fetches from remote and saves to local when fetch type is remote`() = runTest {
        val fetchType = FetchType.Remote
        val recipe = provideRecipeResult()
        val entity = provideRecipeResult().toRecipeResultEntity()

        coEvery { dataStore.data } returns flowOf( SelectedChipPreferences())
        coEvery { repository.fetchRecipeResultRemote(applyQuery()) } returns Result.success(recipe)
        coEvery { repository.insertRecipeResult(entity) } returns Unit

        useCase.invoke(fetchType)
            .onSuccess {
                it.recipes.size shouldBe recipe.recipes.size
                it.recipes[0].recipeId shouldBe recipe.recipes[0].recipeId
            }

        coVerify(Ordering.ORDERED) {
            dataStore.data
            repository.fetchRecipeResultRemote(applyQuery())
            repository.insertRecipeResult(entity)
        }
    }

    @Test
    fun `invoke fetches from remote and saves to local when fetch type is local but local data is empty`() = runTest {
        val fetchType = FetchType.Local
        val recipe = provideRecipeResult()
        val entity = provideRecipeResult().toRecipeResultEntity()

        coEvery { repository.loadRecipeResultLocal() } returns emptyList()
        coEvery { dataStore.data } returns flowOf( SelectedChipPreferences())
        coEvery { repository.fetchRecipeResultRemote(applyQuery()) } returns Result.success(recipe)
        coEvery { repository.insertRecipeResult(entity) } returns Unit

        useCase.invoke(fetchType)
            .onSuccess {
                it.recipes.size shouldBe recipe.recipes.size
                it.recipes[0].recipeId shouldBe recipe.recipes[0].recipeId
            }

        coVerify(Ordering.ORDERED) {
            repository.loadRecipeResultLocal()
            dataStore.data
            repository.fetchRecipeResultRemote(applyQuery())
            repository.insertRecipeResult(entity)
        }
    }

    @Test
    fun `invoke fetch remote fails, local save is not called and the exception is propagated to viewModel`() = runTest {
        val fetchType = FetchType.Remote
        val recipe = provideRecipeResult()
        val entity = provideRecipeResult().toRecipeResultEntity()

        coEvery { dataStore.data } returns flowOf( SelectedChipPreferences())
        coEvery { repository.fetchRecipeResultRemote(applyQuery()) } returns Result.failure(Exception())

        val result = useCase.invoke(fetchType)
        result.isSuccess shouldBe false
        result.isFailure shouldBe true

        coVerify(Ordering.ORDERED) {
            dataStore.data
            repository.fetchRecipeResultRemote(applyQuery())
        }

        coVerify(exactly = 0) {
            repository.insertRecipeResult(entity)
        }
    }

    @Test
    fun `invoke fetches search recipe from remote when fetch type is search`() = runTest {
        val fetchType = FetchType.Search
        fetchType.data = searchString

        val recipe = provideRecipeResult()

        coEvery { repository.fetchSearchedRecipesResult(applySearchQuery()) } returns Result.success(recipe)

        useCase.invoke(fetchType)
            .onSuccess {
                it.recipes.size shouldBe recipe.recipes.size
                it.recipes[0].recipeId shouldBe recipe.recipes[0].recipeId
            }

        coVerify(Ordering.ORDERED) {
            repository.fetchSearchedRecipesResult(applySearchQuery())
        }
    }

    companion object {
        fun applyQuery(): Map<String, String> {
            val query: HashMap<String, String> = hashMapOf()
            query["number"] = "20"
            query["apiKey"] = "apiKey"
            query["type"] = "main course"
            query["diet"] = "gluten free"
            query["addRecipeInformation"] = "true"
            query["fillIngredients"] = "true"
            return query
        }

        const val searchString = "apple"

        fun applySearchQuery(): Map<String, String> {
            val query: HashMap<String, String> = hashMapOf()
            query["query"] = "apple"
            query["number"] = "20"
            query["apiKey"] = "apiKey"
            query["addRecipeInformation"] = "true"
            query["fillIngredients"] = "true"
            return query
        }
    }
}
