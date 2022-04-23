package com.abhi41.foodrecipe.screens.detailScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.IngredientsAdapter
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.Constants
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)
        setupRecyclerView(view)

        myBundle?.extendedIngredients?.let { ingredient ->
            mAdapter.setData(ingredient)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        view.rv_ingredients.adapter = mAdapter
        view.rv_ingredients.layoutManager = LinearLayoutManager(requireContext())
    }
}