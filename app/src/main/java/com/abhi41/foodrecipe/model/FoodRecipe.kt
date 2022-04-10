package com.abhi41.foodrecipe.model


import com.google.gson.annotations.SerializedName

data class FoodRecipe(

    @SerializedName("results")
    val results: List<Result>

)