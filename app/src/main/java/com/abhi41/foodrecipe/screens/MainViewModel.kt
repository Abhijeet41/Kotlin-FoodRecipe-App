package com.abhi41.foodrecipe.screens

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity
import com.abhi41.foodrecipe.data.Repository
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.utils.CheckConnection
import com.abhi41.foodrecipe.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

//because of @ViewModelInject we don't need to create viewmodel factory
//note this @ViewModelInject is depreciated instead of this use @HiltViewModel
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {

    /**  ROOM DATABASE */

    //we get data from flow so convert it in live data we used .asLiveData()
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoriteEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    /* --------------- functions for add favorite recipes ------------- */
    fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoriteEntity)
        }

    fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoriteEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    /**  RETROFIT */
    var recipiesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    //search recipes response
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()


    fun getRecipes(quries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(quries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }


    private suspend fun getRecipesSafeCall(quries: Map<String, String>) {

        recipiesResponse.value = NetworkResult.Loading()//for show progressbar
        if (CheckConnection.hasInternetConnection(application = getApplication<Application>())) {

            try {
                /* online getting food recipes */
                val response = repository.remote.getRecepies(quries = quries)
                recipiesResponse.value = handleFoodRecipesResponse(response)

                /*---------- Caching FoodRecipes------------- */
                val foodRecipe = recipiesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipiesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipiesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()

        if (CheckConnection.hasInternetConnection(application = getApplication<Application>())) {
            try {
                val response = repository.remote.searchRecipes(quries = searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)
                //note we don't need caching here
            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Connection Timeout!")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }


    // we don't need this any more because we created seperate class CheckConnection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capebilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capebilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capebilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capebilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}