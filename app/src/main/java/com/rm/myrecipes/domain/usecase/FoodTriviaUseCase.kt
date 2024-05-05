package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.common.Constants
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.repository.FoodTriviaRepository
import javax.inject.Inject

class FoodTriviaUseCase @Inject constructor(
    private val foodTriviaRepository: FoodTriviaRepository
) {
    suspend operator fun invoke(): Result<FoodTrivia> {
        return foodTriviaRepository.getFoodTrivia(Constants.API_KEY)
    }
}