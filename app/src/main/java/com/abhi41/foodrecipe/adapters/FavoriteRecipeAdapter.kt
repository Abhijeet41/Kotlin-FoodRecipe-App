package com.abhi41.foodrecipe.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.databinding.FavoriteRecipeRowBinding
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.screens.favorites.FavouriteRecipesFragmentDirections
import com.abhi41.foodrecipe.utils.PrintMessage
import com.abhi41.foodrecipe.utils.RecipesDiffUtils

class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipeAdapter.MyViewHolder>(), ActionMode.Callback {
    private var multiSelection = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var favoriteRecipes = emptyList<FavoriteEntity>()

    class MyViewHolder(
        val binding: FavoriteRecipeRowBinding

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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        saveItemState(favoriteRecipes[position], holder) //fix scroll bug
        val currentRecipe = favoriteRecipes.get(position)
        holder.bind(currentRecipe)
        /**
         * Single Click Listener
         */
        holder.binding.recipesFavRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }

        }
        /**
         * Long Click Listener
         */
        holder.binding.recipesFavRowLayout.setOnLongClickListener {

            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                //multiSelection = false
                false
            }


        }
    }

    //fix bug
    /* so if we have 9 items in favorite when we long pressed and
     select 1st item automatically 8th item also get selected */
    private fun saveItemState(currentRecipe: FavoriteEntity, holder: MyViewHolder) {
        if (selectedRecipes.contains(currentRecipe)) {
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.selectedStrokeColor)
        } else {
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }


    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle() //when there is no selected item then  dismiss actionbar
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.selectedStrokeColor)
            applyActionModeTitle() //when there is no selected item then  dismiss actionbar
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.recipesFavRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favRowCardview.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_menu) {
            selectedRecipes.forEach { favoriteEntity ->
                mainViewModel.deleteFavoriteRecipe(favoriteEntity)
            }
            showSnacBar("${selectedRecipes.size} Recipes removed")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { myViewHolder ->
            changeRecipeStyle(myViewHolder, R.color.cardBackgroundColor, R.color.strokeColor)
        }

        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteRecipes: List<FavoriteEntity>) {
        val favRecipesDiffutil = RecipesDiffUtils(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffutil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnacBar(message: String) {
        //  Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT).setAction("Okay"){}
        PrintMessage.showSnackBarAction(message, rootView, "Okay")
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}