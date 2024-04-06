package com.rm.myrecipes.ui.fragments.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.usecase.FavouriteRecipeUseCase
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavouriteRecipesViewModel @Inject constructor(
    private val favouriteRecipeUseCase: FavouriteRecipeUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _favouriteRecipesState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Loading)
    val favouriteRecipesState: Flow<UiState<List<Recipe>>>
        get() = _favouriteRecipesState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
    )

    var recipeSaveState = RecipeSaveState()

    private var lastFetchJob: Job? = null

    init { fetchFavouriteRecipes() }

    private fun fetchFavouriteRecipes() {
        lastFetchJob?.cancel()

        lastFetchJob = viewModelScope.launch(dispatcher) {
            favouriteRecipeUseCase()
                .map {
                    UiState.Success(it) as UiState<List<Recipe>>
                }
                .onCompletion {
                    Timber.d("RecipeFavourites: Flow has completed.")
                }
                .catch {throwable ->
                    Timber.d("RecipeFavourites: Caught: $throwable")
                    emit(UiState.Error("Something went wrong"))
                }
                .collect {
                    _favouriteRecipesState.value = it
                }
        }
    }

    fun saveFavouriteRecipe(recipe: Recipe) {
        viewModelScope.launch(dispatcher) {
            try {
                favouriteRecipeUseCase.insertFavouriteRecipe(recipe)
            } catch (e: Exception) {
                Timber.d("RecipeFavourite: ${e.message}")
            }
        }
    }

    fun deleteFavouriteRecipe(recipe: Recipe) {
        viewModelScope.launch(dispatcher) {
            try {
                favouriteRecipeUseCase.deleteRecipe(recipe)
            } catch (e: Exception) {
                Timber.d("RecipeFavourite: ${e.message}")
            }
        }
    }

    fun deleteAllFavouriteRecipe() {
        viewModelScope.launch(dispatcher) {
            try {
                favouriteRecipeUseCase.deleteAlRecipes()
            } catch (e: Exception) {
                Timber.d("RecipeFavourite: ${e.message}")
            }
        }
    }

    fun invalidateRecipeSaveState() {
        Timber.d("Invalidate recipe saved state")
        recipeSaveState.savedId = null
        recipeSaveState.isSaved = false
    }
}

data class RecipeSaveState(
    var savedId: Int? = null,
    var isSaved: Boolean = false
)
