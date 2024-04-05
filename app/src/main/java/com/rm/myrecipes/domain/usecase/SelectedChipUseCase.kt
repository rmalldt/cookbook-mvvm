package com.rm.myrecipes.domain.usecase

import com.rm.myrecipes.data.DataStoreRepository
import com.rm.myrecipes.data.SelectedChipPreferences
import javax.inject.Inject

class SelectedChipUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    operator fun invoke() = dataStoreRepository.data

    suspend fun saveSelectedChipTypes(
        mealType: String,
        mealId: Int,
        dietType: String,
        dietId: Int
    ) {
        dataStoreRepository.saveSelectedChipPreferences(
            SelectedChipPreferences(mealType, mealId, dietType, dietId)
        )
    }
}