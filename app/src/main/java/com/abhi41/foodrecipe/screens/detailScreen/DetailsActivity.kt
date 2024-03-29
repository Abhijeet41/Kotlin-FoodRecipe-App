package com.abhi41.foodrecipe.screens.detailScreen

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.PagerAdapter
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.databinding.ActivityDetailsBinding
import com.abhi41.foodrecipe.screens.MainViewModel
import com.abhi41.foodrecipe.utils.BaseActivity
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.PrintMessage
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailsActivity : BaseActivity() {
    private val TAG = "DetailsActivity"
    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       // throw  RuntimeException("Test Crash");

        val fragments = ArrayList<Fragment>()
        fragments.add(OverViewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        //get data from (Result) from Recipesfragment using args and pass data using bundle
        val resultBundle = Bundle()
        try {
            resultBundle.putParcelable(Constants.RECIPE_RESULT_KEY, args.result)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //now we have fragments,titles,resultBundle so we can initialize PagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        val pagerAdapter = PagerAdapter(resultBundle, fragments, this)
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles.get(position)
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorite_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorite_menu && !recipeSaved) {
            saveToFavorite(item)
        } else if (item.itemId == R.id.save_to_favorite_menu && recipeSaved) {
            deleteFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    //check whether recipe is added to favorite and change menu icon color
    private fun checkSavedRecipes(menuItem: MenuItem) {
        lifecycleScope.launch {
            mainViewModel.readFavoriteRecipes.observe(this@DetailsActivity) { favoriteEntity ->
                try {
                    for (savedFavorite in favoriteEntity) {
                        if (savedFavorite.result.recipeId == args.result.recipeId) {
                            changeMenuItemColor(menuItem, R.color.yellow)
                            savedRecipeId = savedFavorite.id
                            recipeSaved = true
                            break // every time else block gets called so white color set on saved favorite icon also
                        } else {
                            changeMenuItemColor(menuItem, R.color.white)
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }

            }
        }
    }

    //save to favorite and change color
    private fun saveToFavorite(item: MenuItem) {
        var favoriteEntity = FavoriteEntity(
            0,
            args.result,
        )
        mainViewModel.insertFavoriteRecipe(favoriteEntity)

        changeMenuItemColor(item, R.color.yellow)
        PrintMessage.showSnackBarAction(
            "Recipe Saved",
            binding.detailsLayout,
            "Okay"
        )
        recipeSaved = true
    }

    //remove from favorite change color
    private fun deleteFromFavorite(item: MenuItem) {
        var favoriteEntity = FavoriteEntity(
            savedRecipeId,
            args.result,
        )
        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item, R.color.white)
        PrintMessage.showSnackBarAction(
            "Recipe from Favorite",
            binding.detailsLayout,
            "Okay"
        )
        recipeSaved = false
    }


    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}