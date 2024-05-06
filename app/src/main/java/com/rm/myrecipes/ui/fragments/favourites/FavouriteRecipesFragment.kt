package com.rm.myrecipes.ui.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.myrecipes.databinding.FragmentFavouriteRecipesBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.favourites.adapter.FavouriteRecipesAdapter
import com.rm.myrecipes.ui.fragments.favourites.viewmodel.FavouriteRecipesViewModel
import com.rm.myrecipes.ui.utils.safeCollect
import com.rm.myrecipes.ui.utils.setVisible
import com.rm.myrecipes.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteRecipesViewModel by viewModels()

    private lateinit var favouriteRecipesAdapter: FavouriteRecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.fetchFavouriteRecipes()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouriteRecipesAdapter = FavouriteRecipesAdapter(requireActivity()) { selectedRecipes ->
            displayDialog(selectedRecipes)
        }

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

    private fun displayDialog(recipes: List<Recipe>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("Dialog", ArrayList(recipes))
        val dialog = DeletionAlertDialogFragment.newInstance(bundle)
        dialog.show(childFragmentManager, "Dialog")
    }

    private fun initRecyclerView() {
        binding.favoriteRecipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouriteRecipesAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favouriteRecipesAdapter.clearContextualActionMode()
        _binding = null
    }
}
