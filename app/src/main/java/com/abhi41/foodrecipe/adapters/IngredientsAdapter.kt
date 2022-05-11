package com.abhi41.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.databinding.IngredientsRowLayoutBinding
import com.abhi41.foodrecipe.model.ExtendedIngredient
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.RecipesDiffUtils
import java.util.*


class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            IngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = ingredientList.get(position)
        holder.binding.imgIngredient.load(Constants.BASE_IMAGE_URL + ingredient.image) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.txtIngredient.text = ingredient.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        holder.binding.txtIngredientAmount.text = ingredient.amount.toString()
        holder.binding.txtIngredientUnit.text = ingredient.unit
        holder.binding.txtIngredientConsistency.text = ingredient.consistency
        holder.binding.txtIngredientOriginal.text = ingredient.original
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val ingredientsDiffutil = RecipesDiffUtils(ingredientList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffutil)
        ingredientList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

}