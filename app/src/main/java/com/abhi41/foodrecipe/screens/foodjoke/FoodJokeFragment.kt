package com.abhi41.foodrecipe.screens.foodjoke

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.databinding.FragmentFoodJokeBinding
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.NetworkResult
import com.abhi41.foodrecipe.utils.PrintMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {
    private val TAG = "FoodJokeFragment"

    //there is two way to intialize viewmodel by dagger hilt and viewmodel provider
    private val mainviewModel by viewModels<MainViewModel>()
    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private var foodJoke: String = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainviewModel

        setHasOptionsMenu(true)

        apiRequest()
        observers()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observers() {
        mainviewModel.foodJokeResponse.observe(requireParentFragment().viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.txtFoodJoke.setText(response.data?.text)
                    if (response.data != null) {
                        foodJoke = response.data.text!!
                    }
                }
                is NetworkResult.Error -> {
                    PrintMessage.showToastMessage(response.message, requireContext())
                    loadDataFromCache() //if there is error load from cache
                }
                is NetworkResult.Loading -> {
                    PrintMessage.messageLogD(TAG, "Loading")
                }
            }
        }
    }

    private fun apiRequest() {
        mainviewModel.getFoodJoke(Constants.API_KEY)
    }

    private fun loadDataFromCache() {
        mainviewModel.readFoodJoke.observe(requireParentFragment().viewLifecycleOwner) { database ->
            lifecycleScope.launch {
                if (!database.isNullOrEmpty()) {
                    try {
                        binding.txtFoodJoke.text = database.get(0).foodJoke.text
                        foodJoke = database[0].foodJoke.text!!
                    } catch (e: Exception) {
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null //to avoid memory leaks
    }
}