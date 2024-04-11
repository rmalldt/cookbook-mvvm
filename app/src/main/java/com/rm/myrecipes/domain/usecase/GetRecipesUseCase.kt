package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.domain.repository.RecipeResultRepository
import com.rm.myrecipes.ui.common.FetchState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeResultRepository: RecipeResultRepository
) {

    operator fun invoke(fetchState: FetchState): Flow<RecipeResult> {
        return recipeResultRepository.getRecipeResult(fetchState)
    }
}