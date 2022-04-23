package com.abhi41.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.model.ExtendedIngredient
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.RecipesDiffUtils
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = ingredientList.get(position)
        holder.itemView.imgIngredient.load(Constants.BASE_IMAGE_URL+ingredient.image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.itemView.txtIngredient.setText(ingredient.name.capitalize())
        holder.itemView.txtIngredientAmount.setText(ingredient.amount.toString())
        holder.itemView.txtIngredientUnit.setText(ingredient.unit)
        holder.itemView.txtIngredientConsistency.setText(ingredient.consistency)
        holder.itemView.txtIngredientOriginal.setText(ingredient.original)
    }

    override fun getItemCount(): Int {
       return ingredientList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>){
        val ingredientsDiffutil = RecipesDiffUtils(ingredientList,newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffutil)
        ingredientList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

}