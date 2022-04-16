package com.abhi41.foodrecipe.screens.recipies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abhi41.foodrecipe.data.DataStoreRepository
import com.abhi41.foodrecipe.data.MealAndDietType
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.abhi41.foodrecipe.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.abhi41.foodrecipe.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    //read data preferences from repository class
    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    //save meal data dataStore preferences
    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch (Dispatchers.IO){
            dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[Constants.QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFO] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    //in java
/*    private HashMap<String, String> applyQueries() {
        HashMap<String,String> quries = new HashMap<String,String>;
        quries.add("key", "value");
        return quries
    }*/

}