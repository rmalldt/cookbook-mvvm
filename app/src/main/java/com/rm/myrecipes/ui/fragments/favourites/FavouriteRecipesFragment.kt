package com.rm.myrecipes.ui.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.myrecipes.databinding.FragmentFavouriteRecipesBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.details.viewmodel.FavouriteRecipesViewModel
import com.rm.myrecipes.ui.fragments.favourites.adapter.FavouriteRecipesAdapter
import com.rm.myrecipes.ui.utils.safeCollect
import com.rm.myrecipes.ui.utils.setVisible
import com.rm.myrecipes.ui.utils.snackBar
import com.rm.myrecipes.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavouriteRecipesViewModel

    private lateinit var favouriteRecipesAdapter: FavouriteRecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[FavouriteRecipesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouriteRecipesAdapter = FavouriteRecipesAdapter(requireActivity(),
            { recipe -> viewModel.deleteFavouriteRecipe(recipe) },
            { size -> binding.root.snackBar("$size Recipe(s) removed") }
        )

        initRecyclerView()

        safeCollect(viewModel.favouriteRecipesState) {
            render(it)
        }
    }

    private fun render(uiState: UiState<List<Recipe>>) {
        when (uiState) {
            is UiState.Loading -> {}

            is UiState.Success -> {
                if (uiState.data.isEmpty()) {
                    binding.ivNoFavoriteRecipes.setVisible()
                    binding.tvNoFavoriteRecipes.setVisible()
                }
                favouriteRecipesAdapter.recipeList = uiState.data
            }

            is UiState.Error -> {
                binding.ivNoFavoriteRecipes.setVisible()
                binding.tvNoFavoriteRecipes.setVisible()
                requireContext().toast(uiState.message)
            }
        }
    }

    private fun initRecyclerView() {
        binding.favoriteRecipesRecyclerView.apply {
            adapter = favouriteRecipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favouriteRecipesAdapter.clearContextualActionMode()
        _binding = null
    }
}