package com.abhi41.foodrecipe.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.utils.NetworkResult

class RecipesBinding {
    companion object {

        /*  Insted of using this two method separate method we can done this in one method
          @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
           @JvmStatic
           fun errorImageViewVisibility(
               imageView: ImageView,
               apiResponse: NetworkResult<FoodRecipe>?,
               database: List<RecipesEntity>?
           ) {
               if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                   imageView.visibility = View.VISIBLE
               }else if (apiResponse is NetworkResult.Loading){
                   imageView.visibility = View.GONE
               }else if (apiResponse is NetworkResult.Success){
                   imageView.visibility = View.GONE
               }
           }

           @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
           @JvmStatic
           fun errorTextViewVisibility(
               textView: TextView,
               apiResponse: NetworkResult<FoodRecipe>?,
               database: List<RecipesEntity>?
           ){
               if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                   textView.visibility = View.VISIBLE
                   textView.text = apiResponse.message.toString()
               }else if (apiResponse is NetworkResult.Loading){
                   textView.visibility = View.GONE
               }else if (apiResponse is NetworkResult.Success){
                   textView.visibility = View.GONE
               }
           }
           */

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }

    }
}