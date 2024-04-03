package com.rm.myrecipes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.domain.data.Recipes
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.domain.usecase.SelectedChipUseCase
import com.rm.myrecipes.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val selectedChipUseCase: SelectedChipUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _recipesState = MutableStateFlow<UiState<Recipes>>(UiState.Loading)
    val recipesState: Flow<UiState<Recipes>> get() = _recipesState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init { fetchRecipes(false) }

    private var lastFetchJob: Job? = null

    fun fetchRecipes(applied: Boolean) {
        lastFetchJob?.cancel()
        lastFetchJob = viewModelScope.launch(dispatcher) {
            getRecipesUseCase.invoke(applied)
                .map { recipesList ->
                    UiState.Success(recipesList) as UiState<Recipes>
                }
                .onCompletion {
                    Timber.tag("Recipe").d("Flow has completed.")
                }
                .catch {throwable ->
                    Timber.tag("Recipe").d("Caught: $throwable")
                    emit(UiState.Error("Something went wrong"))
                }
                .collect { state ->
                    Timber.tag("Recipe").d("database triggered")
                    _recipesState.value = state
                }
        }
    }

    val selectedChipState: Flow<UiState<SelectedChipPreferences>> = selectedChipUseCase.invoke()
        .map { preference ->
            UiState.Success(preference) as UiState<SelectedChipPreferences>
        }
        .catch {throwable ->
            Timber.tag("Recipe").d("Caught: $throwable")
        }

    fun saveSelectedChipTypes(
        mealType: String,
        mealId: Int,
        dietType: String,
        dietId: Int,
        block: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                selectedChipUseCase.saveSelectedChipTypes(mealType, mealId, dietType, dietId)
            }.join()

            block()
        }
    }
}
