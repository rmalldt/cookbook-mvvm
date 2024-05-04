package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.data.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SelectedChipUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) {
    operator fun invoke() = dataStoreRepository.data

    suspend fun saveSelectedChipTypes(
        mealType: String,
        mealId: Int,
        dietType: String,
        dietId: Int
    ) = withContext(dispatcher) {
        dataStoreRepository.saveSelectedChipPreferences(
            SelectedChipPreferences(mealType, mealId, dietType, dietId)
        )
    }
}