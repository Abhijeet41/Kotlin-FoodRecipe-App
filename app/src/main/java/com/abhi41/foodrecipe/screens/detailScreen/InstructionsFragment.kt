package com.abhi41.foodrecipe.screens.detailScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.abhi41.foodrecipe.databinding.FragmentInstructionsBinding
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.Constants


class InstructionsFragment : Fragment() {
    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        binding.webInstruction.webViewClient = object : WebViewClient() {}
        val webSiteUrl: String = myBundle?.sourceUrl ?: ""
        binding.webInstruction.loadUrl(webSiteUrl)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}