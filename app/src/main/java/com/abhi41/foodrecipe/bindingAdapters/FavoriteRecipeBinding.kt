package com.abhi41.foodrecipe.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.adapters.FavoriteRecipeAdapter
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity

class FavoriteRecipeBinding {

    companion object {
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
    }

}