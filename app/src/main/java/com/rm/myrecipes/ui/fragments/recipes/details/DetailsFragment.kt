package com.rm.myrecipes.ui.fragments.recipes.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.rm.myrecipes.databinding.FragmentDetailsBinding
import com.rm.myrecipes.ui.fragments.recipes.details.adapter.ViewPagerAdapter
import com.rm.myrecipes.ui.fragments.recipes.details.ingredients.IngredientsFragment
import com.rm.myrecipes.ui.fragments.recipes.details.instructions.InstructionsFragment
import com.rm.myrecipes.ui.fragments.recipes.details.overview.OverviewFragment
import timber.log.Timber

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val recipeArg by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Recipe: ${recipeArg.recipe}")
        setViewPagerTabLayout()
    }

    private fun setViewPagerTabLayout() {
        val fragments = listOf(
            OverviewFragment(),
            IngredientsFragment(),
            InstructionsFragment()
        )
        val bundle = Bundle()
        bundle.putParcelable("recipe", recipeArg.recipe)
        viewPagerAdapter = ViewPagerAdapter(fragments, bundle, childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Overview"
                1 -> tab.text = "Ingredients"
                2 -> tab.text = "Instructions"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
