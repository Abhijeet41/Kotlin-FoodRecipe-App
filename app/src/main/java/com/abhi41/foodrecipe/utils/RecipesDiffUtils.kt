package com.abhi41.foodrecipe.utils

import androidx.recyclerview.widget.DiffUtil

class RecipesDiffUtils<T>(
    //we replace Result model with generic T
    // because we use diffutil in Ingredients Adapter as well with different model class like Extendedgredient
    private val oldRecipeList: List<T>,
    private val newRecipeList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldRecipeList.size
    }

    override fun getNewListSize(): Int {
        return newRecipeList.size
    }

    //=== operator is used to compare the reference of two variable or object
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRecipeList[oldItemPosition] === newRecipeList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRecipeList[oldItemPosition] == newRecipeList[newItemPosition]
    }


}