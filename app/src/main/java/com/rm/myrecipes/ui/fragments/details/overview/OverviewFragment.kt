package com.rm.myrecipes.ui.fragments.details.overview

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentOverviewBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.utils.extension.loadImage
import com.rm.myrecipes.ui.utils.common.parseHtml
import com.rm.myrecipes.ui.utils.extension.resetImageViewAndTextViewColor

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecipeDetails()
    }

    private fun setRecipeDetails() {
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("recipe", Recipe::class.java)
        } else {
            arguments?.getParcelable("recipe")
        }

        recipe?.let {
            with(binding) {
                ivOverview.loadImage(
                    it.image,
                    R.drawable.ic_loading_placeholder,
                    R.drawable.ic_loading_placeholder,
                )

                tvCookingTime.text = it.readyInMinutes.toString()
                tvOverviewLikes.text = it.aggregateLikes.toString()
                tvOverviewTitle.text = it.title

                val colorId = R.color.green
                resetImageViewAndTextViewColor(it.vegetarian, ivVegetarian, tvVegetarian, colorId, colorId)
                resetImageViewAndTextViewColor(it.vegan, ivVegan, tvVegan, colorId, colorId)
                resetImageViewAndTextViewColor(it.dairyFree, ivDairyFree, tvDairyFree, colorId, colorId)
                resetImageViewAndTextViewColor(it.glutenFree, ivGlutenFree, tvGlutenFree, colorId, colorId)
                resetImageViewAndTextViewColor(it.veryHealthy, ivHealthy, tvHealthy, colorId, colorId)
                resetImageViewAndTextViewColor(it.cheap, ivCheap, tvCheap, colorId, colorId)
                parseHtml(tvRecipeDescription, it.summary)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}