package com.abhi41.foodrecipe.screens.recipies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.RecipesAdapter
import com.abhi41.foodrecipe.databinding.FragmentRecipesBinding
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.NetworkResult
import com.abhi41.foodrecipe.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private val TAG = "RecipesFragment"

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipesViewModel
  // private lateinit var mView: View
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
      //  mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this //we use this because we use live data objects
        binding.mainViewModel = mainViewModel
        setupRecyclerview()
        // requestApiData() we have to remove this because now read data from database
        readDataBase() //now retrive data from database instead from network
        observers()

        return binding.root;
    }


    private fun setupRecyclerview() {
        //replace all mView with binding
        binding.rvRecipes.adapter = mAdapter
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun requestApiData() {
        Log.d(TAG, "requestApiData: called")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
    }

    private fun readDataBase() {
        /* Our mainViewModel.readRecipes is not a suspend fun hence we need to run at background thread
        that why we use lifecycleScope */
        lifecycleScope.launch {
            //IMP : we replace observe with observeOnce because  we need to observe this once only
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->

                if (database.isNotEmpty()) {
                    Log.d(TAG, "readDataBase: called")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }

            }
        }
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
                    loadDataFromCache() //if exception occur then fetch data from database
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

    fun loadDataFromCache() {
        //our mainViewModel.readRecipes is not a suspend fun hence we need to run at background thread
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->

                if (database.isNotEmpty()) {
                    Log.d(TAG, "readDataBase: called")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }

            }
        }
    }

    private fun showShimmerEffect() {
        binding.rvRecipes.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.rvRecipes.rv_recipes.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // this to avoid memory leaks
    }
}