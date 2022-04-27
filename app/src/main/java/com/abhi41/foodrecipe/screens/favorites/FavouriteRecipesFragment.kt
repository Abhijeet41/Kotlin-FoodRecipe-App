package com.abhi41.foodrecipe.screens.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.FavoriteRecipeAdapter
import com.abhi41.foodrecipe.databinding.FragmentFavouriteRecipesBinding
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.utils.PrintMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favourite_recipes.view.*

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter: FavoriteRecipeAdapter by lazy {
        FavoriteRecipeAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // val view = inflater.inflate(R.layout.fragment_favourite_recipes, container, false)
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setHasOptionsMenu(true)

        setupRecyclerView(binding.rvFavoriteRecipes)
        ///observer() we don't need this because we observe it from FavoriteRecipeBinding class
        return binding.root
    }

/*    private fun observer() {
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) { favoriteEntity ->
            mAdapter.setData(favoriteEntity)
        }
    }*/

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar() {
        PrintMessage.showSnackBarAction("All Recipes Removed.", requireContext(), binding.root, "Okay")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

}