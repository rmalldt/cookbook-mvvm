package com.rm.myrecipes.domain.repository

import com.rm.myrecipes.data.BaseRepository
import com.rm.myrecipes.domain.data.FoodTrivia
import kotlinx.coroutines.flow.Flow

interface FoodTriviaRepository {

    fun getFoodTrivia(apiKey: String): Flow<FoodTrivia>
}