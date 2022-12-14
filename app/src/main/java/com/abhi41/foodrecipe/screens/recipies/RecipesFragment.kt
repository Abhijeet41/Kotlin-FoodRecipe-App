package com.abhi41.foodrecipe.screens.recipies

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.RecipesAdapter
import com.abhi41.foodrecipe.databinding.FragmentRecipesBinding
import com.abhi41.foodrecipe.utils.NetworkListener
import com.abhi41.foodrecipe.utils.NetworkResult
import com.abhi41.foodrecipe.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val TAG = "RecipesFragment"
    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
   // private lateinit var mainViewModel: MainViewModel
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var recipeViewModel: RecipesViewModel

    // private lateinit var mView: View
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize view model
        //mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //  mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this //we use this because we use live data objects
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        setupRecyclerview()
        // requestApiData() we have to remove this because now read data from database

        observers()
        clickListeners()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view != null){
            lifecycleScope.launchWhenStarted { //insted of launch because its crashed when turn on internet connection
                networkListener = NetworkListener()
                networkListener.checkNetworkAvailability(requireContext())
                    .collect { status ->
                        Log.d(TAG, "networkListener: $status")
                        recipeViewModel.networkStatus = status
                        recipeViewModel.showNetworkStatus()

                        readDataBase() //now retrieve data from database instead from network
                    }
            }
           
        }

    }

    private fun clickListeners() {
        binding.fabRecipes.setOnClickListener {
            if (recipeViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet2)
            } else {
                recipeViewModel.showNetworkStatus()
            }

        }
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

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipeViewModel.applySearchQuery(searchQuery))
    }

    private fun readDataBase() {
        /* Our mainViewModel.readRecipes is not a suspend fun hence we need to run at background thread
        that why we use lifecycleScope */
        if (binding.root != null){
            lifecycleScope.launch {
                //IMP : we replace observe with observeOnce because  we need to observe this once only
                mainViewModel.readRecipes.observeOnce(requireParentFragment().viewLifecycleOwner) { database ->

                    //checking if database is null and is not navigate from bottomsheet
                    if (database.isNotEmpty() && !args.backFromBottomSheet) {
                        Log.d(TAG, "readDataBase: called")
                        mAdapter.setData(database[0].foodRecipe)
                        hideShimmerEffect()
                    } else {
                        requestApiData()
                    }

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
        recipeViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipeViewModel.backOnline = it
        }

        //observe search api
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let {
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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


    private fun loadDataFromCache() {
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
        binding.rvRecipesShimmer.startShimmer()
        binding.rvRecipes.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.rvRecipesShimmer.stopShimmer()
        binding.rvRecipes.visibility = View.VISIBLE
    }

    override fun onDestroy() {

        super.onDestroy()
        _binding = null // this to avoid memory leaks

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}