package com.rm.myrecipes.ui.fragments.foodtrivia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.usecase.FoodTriviaUseCase
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FoodTriviaViewModel @Inject constructor(
    private val foodTriviaUseCase: FoodTriviaUseCase,
    private val networkChecker: NetworkChecker,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _foodTriviaState = MutableStateFlow<UiState<FoodTrivia>>(UiState.Loading)
    val foodTriviaState: Flow<UiState<FoodTrivia>>
        get() = _foodTriviaState

    private var lastFetchJob: Job? = null

    fun safeCall() {
        if (networkChecker.hasInternetConnection()) {
            fetchFoodTrivia()
        } else {
            _foodTriviaState.value = UiState.Error("No network connection")
        }
    }

    private fun fetchFoodTrivia() {
        lastFetchJob?.cancel()
        lastFetchJob = viewModelScope.launch(dispatcher) {
            foodTriviaUseCase()
                .map {
                    UiState.Success(it) as UiState<FoodTrivia>
                }
                .onCompletion {
                    Timber.d("RecipeTrivia: Flow has completed.")
                }
                .catch {throwable ->
                    Timber.d("RecipeTrivia: Caught : $throwable")
                    emit(UiState.Error("Something went wrong"))
                }
                .collect { uiState ->
                    _foodTriviaState.value = uiState
                }
        }
    }
}