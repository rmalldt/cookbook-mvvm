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
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private val _foodTriviaState = MutableStateFlow<UiState<FoodTrivia>>(UiState.Loading)
    val foodTriviaState: Flow<UiState<FoodTrivia>>
        get() = _foodTriviaState

    private var lastFetchJob: Job? = null

    fun safeCall() {
        if (networkChecker.hasInternetConnection()) {
            fetchFoodTrivia()
        } else {
            _foodTriviaState.value = UiState.Error("No network connection.")
        }
    }

    private fun fetchFoodTrivia() {
        lastFetchJob?.cancel()
        lastFetchJob = viewModelScope.launch {
            foodTriviaUseCase()
                .onSuccess {
                    _foodTriviaState.value = UiState.Success(it)
                }
                .onFailure {
                    Timber.d("RecipeTrivia: caught: $it")
                    _foodTriviaState.value = UiState.Error("Something went wrong, please try again.")
                }
        }
    }
}