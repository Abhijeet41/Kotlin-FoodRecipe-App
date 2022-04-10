package com.abhi41.foodrecipe.screens

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abhi41.foodrecipe.data.Repository
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.utils.CheckConnection
import com.abhi41.foodrecipe.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
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


    var recipiesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(quries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(quries)
    }

    private suspend fun getRecipesSafeCall(quries: Map<String, String>) {

        recipiesResponse.value = NetworkResult.Loading()//for show progressbar

        if (CheckConnection.hasInternetConnection(application = getApplication<Application>())) {
            try {
                val response = repository.remote.getRecepies(quries = quries)
                recipiesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                recipiesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipiesResponse.value = NetworkResult.Error("No Internet Connection")
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