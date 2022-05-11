package com.abhi41.foodrecipe.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.abhi41.foodrecipe.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.abhi41.foodrecipe.utils.Constants.Companion.PREFERENCES_DIET_TYPE
import com.abhi41.foodrecipe.utils.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.abhi41.foodrecipe.utils.Constants.Companion.PREFERENCE_BACK_ONLINE
import com.abhi41.foodrecipe.utils.Constants.Companion.PREFERENCE_DIET_TYPE_ID
import com.abhi41.foodrecipe.utils.Constants.Companion.PREFERENCE_MEAL_TYPE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.PREFERENCES_NAME
)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    //data store preferences
    /**
     * Note in datastore preferences 1.0.0  some syntax has been changed like create data source and
     * preferencesKey replace by stringPreferencesKey
     */

    //first we need to create PrefKeys like shared preferences
    private object PreferenceKeys {
        // val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE) //syntax has been changed
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE) //mealType
        val selectedMealTypeId = intPreferencesKey(PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCE_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCE_BACK_ONLINE)
    }

    //create data source
    private val dataStore: DataStore<Preferences> = context.dataStore

    //save data in datastore preferences
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKeys.selectedMealType] = mealType
            mutablePreferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            mutablePreferences[PreferenceKeys.selectedDietType] = dietType
            mutablePreferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    //retrieve data from datastore preferences
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // ?: this means if its null then its DEFAULT_MEAL_TYPE (main course)
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    //save back online status in datastore preference
    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)