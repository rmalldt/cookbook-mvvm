package com.rm.myrecipes.ui.fragments.recipes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.domain.usecase.SelectedChipUseCase
import com.rm.myrecipes.ui.common.FetchState
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val selectedChipUseCase: SelectedChipUseCase,
    private val networkChecker: NetworkChecker,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _recipeResultState = MutableStateFlow<UiState<RecipeResult>>(UiState.Loading)
    val recipeResultState: Flow<UiState<RecipeResult>> get() = _recipeResultState

    private var lastFetchJob: Job? = null

    fun fetchSafe(fetchState: FetchState = FetchState.FetchLocal) {
        val isNetwork = networkChecker.hasInternetConnection()
        when {
            isNetwork -> fetchRecipeResult(fetchState)
            else -> fetchRecipeResult(FetchState.FetchLocal)
        }
    }

    private fun fetchRecipeResult(fetchState: FetchState) {
        lastFetchJob?.cancel()
        lastFetchJob = viewModelScope.launch(dispatcher) {
            getRecipesUseCase.invoke(fetchState)
                .map { recipesList ->
                    UiState.Success(recipesList) as UiState<RecipeResult>
                }
                .onCompletion {
                    Timber.d("Recipe: Flow has completed.")
                }
                .catch {throwable ->
                    Timber.d("Recipe: Caught: $throwable")
                    emit(UiState.Error("Something went wrong"))
                }
                .collect { state ->
                    _recipeResultState.value = state
                }
        }
    }

    val selectedChipState: Flow<UiState<SelectedChipPreferences>> = selectedChipUseCase.invoke()
        .map { preference ->
            UiState.Success(preference) as UiState<SelectedChipPreferences>
        }
        .catch {throwable ->
            Timber.d("Recipe: Caught: $throwable")
        }

    fun applyMealDietType(
        mealType: String,
        mealId: Int,
        dietType: String,
        dietId: Int,
        call: () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            launch {
                selectedChipUseCase.saveSelectedChipTypes(mealType, mealId, dietType, dietId)
            }.join()

            call()
        }
    }
}
