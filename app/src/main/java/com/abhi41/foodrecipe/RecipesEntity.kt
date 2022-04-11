package com.abhi41.foodrecipe

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0


}