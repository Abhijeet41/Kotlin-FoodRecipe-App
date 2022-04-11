package com.abhi41.foodrecipe.data.network

import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.data.network.FoodRecipesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecepies(quries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipies(quries)
    }

}