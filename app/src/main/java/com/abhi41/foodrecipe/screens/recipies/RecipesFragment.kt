package com.abhi41.foodrecipe.screens.recipies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.RecipesAdapter
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipesViewModel
    private lateinit var mView: View
    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize view model
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)



        setupRecyclerview()
        requestApiData()
        observers()
        return mView;
    }


    private fun setupRecyclerview() {
        mView.rv_recipes.adapter = mAdapter
        mView.rv_recipes.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
    }

    private fun observers() {
        mainViewModel.recipiesResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        }
    }


    private fun showShimmerEffect() {
        mView.rv_recipes.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView.rv_recipes.hideShimmer()
    }

}