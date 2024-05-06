package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.domain.data.FoodTrivia
import kotlinx.coroutines.flow.Flow

interface FoodTriviaRepository {

    suspend fun getFoodTrivia(): Result<FoodTrivia>
}