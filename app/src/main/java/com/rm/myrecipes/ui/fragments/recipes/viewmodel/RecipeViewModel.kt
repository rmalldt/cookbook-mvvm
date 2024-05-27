package com.rm.myrecipes.ui.fragments.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.domain.usecase.SelectedChipUseCase
import com.rm.myrecipes.ui.common.FetchType
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.network.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val selectedChipUseCase: SelectedChipUseCase,
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private val _recipeResultState = MutableStateFlow<UiState<RecipeResult>>(UiState.Loading)
    val recipeResultState: Flow<UiState<RecipeResult>> get() = _recipeResultState

    private var lastFetchJob: Job? = null

    fun fetchSafe(fetchType: FetchType) {
        when {
            networkChecker.hasInternetConnection() -> fetchRecipe(fetchType)

            else -> fetchRecipe(FetchType.Local)
        }
    }

    private fun fetchRecipe(fetchType: FetchType) {
        lastFetchJob?.cancel()

        lastFetchJob = viewModelScope.launch {
            getRecipesUseCase(fetchType)
                .onSuccess {
                    _recipeResultState.value = UiState.Success(it.copy(recipes = it.recipes))
                }
                .onFailure {
                    Timber.d("Recipe: caught $it")
                    _recipeResultState.value = UiState.Error("Something went wrong. Please check your internet connection.")
                }
        }
    }

    val selectedChipState: Flow<UiState<SelectedChipPreferences>> = selectedChipUseCase.invoke()
        .map { preference ->
            UiState.Success(preference) as UiState<SelectedChipPreferences>
        }
        .catch {
            Timber.d("Recipe: caught: $it")
        }

    fun applyMealDietType(
        mealType: String,
        mealId: Int,
        dietType: String,
        dietId: Int,
        call: () -> Unit
    ) {
        viewModelScope.launch {
            selectedChipUseCase.saveSelectedChipTypes(mealType, mealId, dietType, dietId)
            call()
        }
    }
}
