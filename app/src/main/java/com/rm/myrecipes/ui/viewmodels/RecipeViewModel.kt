package com.rm.myrecipes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.domain.data.Recipes
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.domain.usecase.SelectedChipUseCase
import com.rm.myrecipes.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    private val selectedChipUseCase: SelectedChipUseCase
) : ViewModel() {

    val recipesState: Flow<UiState<Recipes>> = getRecipesUseCase.invoke()
        .map { recipesList ->
            UiState.Success(recipesList) as UiState<Recipes>
        }
        .onCompletion {
            Timber.tag("Recipe").d("Flow has completed.")
        }
        .catch {throwable ->
            Timber.tag("Recipe").d("Caught: $throwable")
            emit(UiState.Error("Something went wrong"))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

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
        dietId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedChipUseCase.saveSelectedChipTypes(mealType, mealId, dietType, dietId)
        }
    }
}