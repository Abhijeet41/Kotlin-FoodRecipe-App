package com.abhi41.foodrecipe.screens.detailScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.Constants
import kotlinx.android.synthetic.main.fragment_over_view.view.*
import org.jsoup.Jsoup


class OverViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_over_view, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        view.imgMain.load(myBundle?.image)
        view.txtTitle.setText(myBundle?.title)//we can also write view.txtTitle.text = myBundle?.title
        view.txtLikes.text = myBundle?.aggregateLikes.toString()
        view.txtTime.text = myBundle?.readyInMinutes.toString()
       // view.txtSummery.text = myBundle?.summary   to fix html tags
        myBundle?.summary.let {
            val summary = Jsoup.parse(it?:"")
            view.txtSummery.setText(summary.text())
        }

        if(myBundle?.vegetarian == true) {
            view.imgVegetarian.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.txtVegetarian.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.vegan == true) {
            view.imgVegan.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.txtVegan.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.glutenFree == true) {
            view.imgGluten_free.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.txtGluten_free.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.dairyFree == true) {
            view.imgDiary.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.txtDiary.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.veryHealthy == true) {
            view.imgHealthy.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.txtHealthy.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if(myBundle?.cheap == true) {
            view.imgCheap.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.txtCheap.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        return view
    }

}