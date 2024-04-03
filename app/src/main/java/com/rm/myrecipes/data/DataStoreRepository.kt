package com.rm.myrecipes.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rm.myrecipes.data.common.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val mealTypeKey = stringPreferencesKey("mealType")
    private val mealIdKey = intPreferencesKey("mealId")
    private val dietTypeKey = stringPreferencesKey("dietType")
    private val dietIdKey = intPreferencesKey("dietId")

    val data: Flow<SelectedChipPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            SelectedChipPreferences(
                preferences[mealTypeKey] ?: Constants.DEFAULT_MEAL_TYPE,
                preferences[mealIdKey] ?: 0,
                preferences[dietTypeKey] ?: Constants.DEFAULT_DIET_TYPE,
                preferences[dietIdKey] ?: 0
            )
        }

    suspend fun saveSelectedChipPreferences(selectedChipPreferences: SelectedChipPreferences) {
        dataStore.edit { preferences ->
            with(selectedChipPreferences) {
                preferences[mealTypeKey] = selectedMealType
                preferences[mealIdKey] = selectedMealId
                preferences[dietTypeKey] = selectedDietType
                preferences[dietIdKey] = selectedDietId
            }
        }
    }
}

data class SelectedChipPreferences(
    val selectedMealType: String = Constants.DEFAULT_MEAL_TYPE,
    val selectedMealId: Int = 0,
    val selectedDietType: String = Constants.DEFAULT_DIET_TYPE,
    val selectedDietId: Int,
)


