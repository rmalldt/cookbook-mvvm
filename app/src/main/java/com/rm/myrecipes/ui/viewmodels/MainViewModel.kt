package com.rm.myrecipes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.Recipes
import com.rm.myrecipes.domain.usecase.GetRecipesUseCase
import com.rm.myrecipes.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {


    val uiState: Flow<UiState<Recipes>> = getRecipesUseCase.invoke(applyQueries())
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

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_NUMBER] = "50"
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = "snack"
        queries[Constants.QUERY_DIET] = "vegan"
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}