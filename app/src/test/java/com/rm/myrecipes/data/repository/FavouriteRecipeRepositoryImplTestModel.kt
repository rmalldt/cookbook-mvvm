package com.rm.myrecipes.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.rm.myrecipes.data.room.LocalDataSource
import com.rm.myrecipes.utils.FakeDatabase
import com.rm.myrecipes.utils.MainDispatcherRule
import com.rm.myrecipes.utils.provideRecipe
import com.rm.myrecipes.utils.provideRecipeResultEntity
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class FavouriteRecipeRepositoryImplTest {

    // Instantiate fakes
    private val fakeDatabase = FakeDatabase()
    private val localDataSource = LocalDataSource(fakeDatabase)

    private lateinit var repository: FavouriteRecipeRepositoryImpl

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = FavouriteRecipeRepositoryImpl(
            localDataSource,
            mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `getFavouriteRecipes returns list of recipes fetched from local`() = runTest {
        // Given
        fakeDatabase.recipeResultEntityMock = {
            listOf(provideRecipeResultEntity())
        }

        // Act & Assert
        repository.getFavouriteRecipes().test {
            val localData = awaitItem()
            localData.size shouldBe 1
            localData[0].recipeId shouldBe 1

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `insertFavouriteRecipes inserts into local`() = runTest {
        // Act & Assert
        repository.insertFavouriteRecipe(provideRecipe())

        fakeDatabase.insertedRecipeEntity shouldBe true
    }

    @Test
    fun `deleteFavouriteRecipes deletes from local`() = runTest {
        // Act & Assert
        repository.deleteFavouriteRecipe(provideRecipe())

        fakeDatabase.deletedRecipeEntity shouldBe true
    }

    @Test
    fun `deleteAllFavouriteRecipes deletes from local`() = runTest {
        // Act & Assert
        repository.deleteAllFavouriteRecipes()

        fakeDatabase.deletedAll shouldBe true
    }
}
