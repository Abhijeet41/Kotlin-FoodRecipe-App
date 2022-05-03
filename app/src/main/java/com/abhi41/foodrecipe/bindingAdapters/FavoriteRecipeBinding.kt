package com.abhi41.foodrecipe.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.adapters.FavoriteRecipeAdapter
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity

class FavoriteRecipeBinding {

    companion object {
        /*
         @BindingAdapter("viewVisibility", "setData", requireAll = false)
             @JvmStatic
             fun setDataAndViewVisibility(
                 view: View,
                 favoriteEntity: List<FavoriteEntity>?,
                 mAdapter: FavoriteRecipeAdapter?
             ) {
                 if (favoriteEntity.isNullOrEmpty()) {
                     when (view) {
                         is ImageView -> {
                             view.visibility = View.VISIBLE
                         }
                         is TextView -> {
                             view.visibility = View.VISIBLE
                         }
                         is RecyclerView -> {
                             view.visibility = View.INVISIBLE
                         }

                     }
                 } else {
                     when (view) {
                         is ImageView -> {
                             view.visibility = View.INVISIBLE
                         }
                         is TextView -> {
                             view.visibility = View.INVISIBLE
                         }
                         is RecyclerView -> {
                             view.visibility = View.VISIBLE
                             mAdapter?.setData(favoriteEntity)
                         }

                     }
                 }
             }
             */

        //note we set requireAll = false because we are not going to use setdata attribute on imageview and textview
        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndVisibility(
            view: View,
            favoriteEntity: List<FavoriteEntity>?,
            mAdapter: FavoriteRecipeAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        if (favoriteEntity != null) {
                            mAdapter?.setData(favoriteEntity)
                        }
                    }
                }
                else -> view.isVisible = favoriteEntity.isNullOrEmpty()
            }
        }
    }

}