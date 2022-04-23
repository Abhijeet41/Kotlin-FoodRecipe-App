package com.abhi41.foodrecipe.screens.detailScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.adapters.PagerAdapter
import com.abhi41.foodrecipe.utils.Constants
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        val adapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}