package com.rm.myrecipes.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentRecipesBinding
import com.rm.myrecipes.domain.data.Recipes
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.NetworkListener
import com.rm.myrecipes.ui.utils.setGone
import com.rm.myrecipes.ui.utils.setVisible
import com.rm.myrecipes.ui.utils.toast
import com.rm.myrecipes.ui.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecipesFragment(
) : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeViewModel: RecipeViewModel
    private val recipeAdapter by lazy { RecipesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        recipeViewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

        lifecycleScope.launch {
            recipeViewModel.recipesState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    render(uiState)
                }
        }

        binding.fabRecipes.setOnClickListener {
            findNavController().navigate(
                RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheetFragment()
            )
        }
    }

    private fun render(uiState: UiState<Recipes>) = with(binding) {
        when (uiState) {
            is UiState.Loading -> {
                ivNoConnection.setGone()
                txtNoConnection.setGone()
            }
            is UiState.Success -> {
                ivNoConnection.setGone()
                txtNoConnection.setGone()
                recipeAdapter.recipeList = uiState.data.recipes
            }
            is UiState.Error -> {
                requireContext().toast(uiState.message)
                ivNoConnection.setVisible()
                txtNoConnection.setVisible()
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
