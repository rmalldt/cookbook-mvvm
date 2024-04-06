package com.rm.myrecipes.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentDetailsContainerBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.details.adapter.ViewPagerAdapter
import com.rm.myrecipes.ui.fragments.details.ingredients.IngredientsFragment
import com.rm.myrecipes.ui.fragments.details.instructions.InstructionsFragment
import com.rm.myrecipes.ui.fragments.details.overview.OverviewFragment
import com.rm.myrecipes.ui.fragments.details.viewmodel.FavouriteRecipesViewModel
import com.rm.myrecipes.ui.utils.safeCollect
import com.rm.myrecipes.ui.utils.setDrawableTint
import com.rm.myrecipes.ui.utils.snackBar
import com.rm.myrecipes.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailsContainerFragment : Fragment() {

    private var _binding: FragmentDetailsContainerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var viewModel: FavouriteRecipesViewModel

    private val arg by navArgs<DetailsContainerFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsContainerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[FavouriteRecipesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPagerTabLayout()
        addMenu()
    }

    private fun setViewPagerTabLayout() {
        val fragments = listOf(
            OverviewFragment(),
            IngredientsFragment(),
            InstructionsFragment()
        )
        val bundle = Bundle()
        bundle.putParcelable("recipe", arg.recipe)
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

    private fun addMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_fragment_menu, menu)
                val menuItem = menu.findItem(R.id.menu_item_save_to_favourites)
                safeCollect(viewModel.favouriteRecipesState) {
                    render(it, menuItem)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val itemId = R.id.menu_item_save_to_favourites
                return if (menuItem.itemId == itemId && viewModel.recipeSaveState.isSaved) {
                    removeFromFavourites(menuItem)
                    true
                } else if (menuItem.itemId == itemId && !viewModel.recipeSaveState.isSaved) {
                    saveToFavourites(menuItem)
                    true
                } else {
                    false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(uiState: UiState<List<Recipe>>, menuItem: MenuItem) {
        when (uiState) {
            is UiState.Loading -> {}

            is UiState.Success -> checkSavedRecipes(uiState, menuItem)

            is UiState.Error -> requireContext().toast(uiState.message)
        }
    }

    private fun checkSavedRecipes(uiState: UiState.Success<List<Recipe>>, menuItem: MenuItem) {
        for (favouriteRecipe in uiState.data) {
            if (favouriteRecipe.recipeId == arg.recipe.recipeId ) {
                menuItem.icon?.setDrawableTint(requireContext(), R.color.yellow)
                viewModel.recipeSaveState.savedId = favouriteRecipe.recipeId
                viewModel.recipeSaveState.isSaved = true
            }
        }
    }

    private fun saveToFavourites(menuItem: MenuItem) {
        viewModel.run {
            saveFavouriteRecipe(arg.recipe)
            recipeSaveState.savedId = arg.recipe.recipeId
            recipeSaveState.isSaved = true
        }
        menuItem.icon.setDrawableTint(requireContext(), R.color.yellow)
        binding.root.snackBar("Recipe saved to favourites.")
    }

    private fun removeFromFavourites(menuItem: MenuItem) {
        viewModel.run {
            deleteFavouriteRecipe(arg.recipe)
            recipeSaveState.savedId = null
            recipeSaveState.isSaved = false
        }
        menuItem.icon.setDrawableTint(requireContext(), R.color.white)
        binding.root.snackBar("Recipe removed from favourites.")
    }

    override fun onStop() {
        super.onStop()
        viewModel.invalidateRecipeSaveState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
