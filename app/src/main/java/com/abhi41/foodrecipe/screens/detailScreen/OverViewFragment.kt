package com.abhi41.foodrecipe.screens.detailScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.abhi41.foodrecipe.R
import com.abhi41.foodrecipe.bindingAdapters.RecipesRowBinding
import com.abhi41.foodrecipe.databinding.FragmentOverViewBinding
import com.abhi41.foodrecipe.databinding.FragmentRecipesBinding
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.Constants
import org.jsoup.Jsoup


class OverViewFragment : Fragment() {
    private var _binding: FragmentOverViewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverViewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        binding.imgMain.load(myBundle?.image)
        binding.txtTitle.setText(myBundle?.title)//we can also write view.txtTitle.text = myBundle?.title
        binding.txtLikes.text = myBundle?.aggregateLikes.toString()
        binding.txtTime.text = myBundle?.readyInMinutes.toString()
        // view.txtSummery.text = myBundle?.summary   to fix html tags
        /*     myBundle?.summary.let { //replace these lines of code in line no. 41
                 val summary = Jsoup.parse(it?:"")
                 binding.txtSummery.setText(summary.text())
             }*/
        RecipesRowBinding.parseHtml(binding.txtSummery, myBundle?.summary)

        /*  insted of this create updateColors function to optimize code
         if (myBundle?.vegetarian == true) {
               binding.imgVegetarian.setColorFilter(
                   ContextCompat.getColor(
                       requireContext(),
                       R.color.green
                   )
               )
               binding.txtVegetarian.setTextColor(
                   ContextCompat.getColor(
                       requireContext(),
                       R.color.green
                   )
               )
           }
           if (myBundle?.vegan == true) {
               binding.imgVegan.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
               binding.txtVegan.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
           }
           if (myBundle?.glutenFree == true) {
               binding.imgGlutenFree.setColorFilter(
                   ContextCompat.getColor(
                       requireContext(),
                       R.color.green
                   )
               )
               binding.txtGlutenFree.setTextColor(
                   ContextCompat.getColor(
                       requireContext(),
                       R.color.green
                   )
               )
           }
           if (myBundle?.dairyFree == true) {
               binding.imgDiary.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
               binding.txtDiary.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
           }
           if (myBundle?.veryHealthy == true) {
               binding.imgHealthy.setColorFilter(
                   ContextCompat.getColor(
                       requireContext(),
                       R.color.green
                   )
               )
               binding.txtHealthy.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
           }
           if (myBundle?.cheap == true) {
               binding.imgCheap.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
               binding.txtCheap.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
           }
           */

        if (myBundle != null) {
            updateColors(myBundle.vegetarian, binding.txtVegetarian, binding.imgVegetarian)
            updateColors(myBundle.vegan, binding.txtVegan, binding.imgVegan)
            updateColors(myBundle.cheap, binding.txtCheap, binding.imgCheap)
            updateColors(myBundle.dairyFree, binding.txtDiary, binding.imgDiary)
            updateColors(myBundle.glutenFree, binding.txtGlutenFree, binding.imgGlutenFree)
            updateColors(myBundle.veryHealthy, binding.txtHealthy, binding.imgHealthy)
        }

        return binding.root
    }

    private fun updateColors(stateOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}