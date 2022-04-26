package com.abhi41.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.databinding.FavoriteRecipeRowBinding
import com.abhi41.foodrecipe.utils.RecipesDiffUtils

class FavoriteRecipeAdapter : RecyclerView.Adapter<FavoriteRecipeAdapter.MyViewHolder>() {

    private var favoriteRecipes = emptyList<FavoriteEntity>()

    class MyViewHolder(
        private val binding: FavoriteRecipeRowBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEntity: FavoriteEntity) {
            binding.favoritesEntity = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectRecipe = favoriteRecipes.get(position)
        holder.bind(selectRecipe)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavoriteRecipes: List<FavoriteEntity>) {
        val favRecipesDiffutil = RecipesDiffUtils(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffutil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}