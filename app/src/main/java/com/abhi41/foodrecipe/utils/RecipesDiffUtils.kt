package com.abhi41.foodrecipe.utils

import androidx.recyclerview.widget.DiffUtil
import com.abhi41.foodrecipe.model.Result

class RecipesDiffUtils(
    private val oldRecipeList: List<Result>,
    private val newRecipeList: List<Result>
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