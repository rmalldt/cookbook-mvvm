package com.rm.myrecipes.ui.fragments.favourites.viewmodel

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavouriteRecipesViewModel @Inject constructor(
    private val favouriteRecipeUseCase: FavouriteRecipeUseCase
) : ViewModel() {

    private val _favouriteRecipesState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Loading)

    val favouriteRecipesState: StateFlow<UiState<List<Recipe>>> = _favouriteRecipesState.asStateFlow()

    var recipeSaveState = RecipeSaveState()

    private var lastFetchJob: Job? = null

    fun fetchFavouriteRecipes() {
        lastFetchJob?.cancel()

        lastFetchJob = viewModelScope.launch {
            favouriteRecipeUseCase.invoke()
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
        viewModelScope.launch {
            try {
                favouriteRecipeUseCase.insertFavouriteRecipe(recipe)
            } catch (e: Exception) {
                Timber.d("RecipeFavourite: ${e.message}")
            }
        }
    }

    fun deleteFavouriteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                favouriteRecipeUseCase.deleteRecipe(recipe)
            } catch (e: Exception) {
                Timber.d("RecipeFavourite: ${e.message}")
            }
        }
    }

    fun deleteAllFavouriteRecipe() {
        viewModelScope.launch {
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
