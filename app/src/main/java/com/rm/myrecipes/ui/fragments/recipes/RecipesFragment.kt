package com.rm.myrecipes.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentRecipesBinding
import com.rm.myrecipes.domain.data.Recipes
import com.rm.myrecipes.ui.common.FetchState
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.utils.setGone
import com.rm.myrecipes.ui.utils.setVisible
import com.rm.myrecipes.ui.utils.toast
import com.rm.myrecipes.ui.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

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
        addMenu()

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
                if (uiState.data.recipes.isNotEmpty()) {
                    recipeAdapter.recipeList = uiState.data.recipes
                } else {
                    recipeViewModel.fetchSafe(FetchState.FetchRemote)
                }

            }
            is UiState.Error -> {
                requireContext().toast(uiState.message)
                recipeViewModel.fetchSafe(FetchState.FetchLocal)
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

    private fun addMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actionbar_menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@RecipesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
