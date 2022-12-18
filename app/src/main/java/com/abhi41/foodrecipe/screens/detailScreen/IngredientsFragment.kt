package com.abhi41.foodrecipe.screens.detailScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhi41.foodrecipe.adapters.IngredientsAdapter
import com.abhi41.foodrecipe.databinding.FragmentIngredientsBinding
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.BaseFragment
import com.abhi41.foodrecipe.utils.Constants


class IngredientsFragment : BaseFragment() {
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)
        setupRecyclerView()

        myBundle?.extendedIngredients?.let { ingredient ->
            mAdapter.setData(ingredient)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvIngredients.adapter = mAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}