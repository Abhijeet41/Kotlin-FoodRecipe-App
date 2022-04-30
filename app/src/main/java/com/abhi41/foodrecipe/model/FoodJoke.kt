package com.abhi41.foodrecipe.model


import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    val text: String?
)