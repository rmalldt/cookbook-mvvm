package com.rm.myrecipes.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentRecipesBinding
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.ui.common.FetchType
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.recipes.adapter.RecipesAdapter
import com.rm.myrecipes.ui.fragments.recipes.observer.DebouncingQueryTextListener
import com.rm.myrecipes.ui.fragments.recipes.viewmodel.RecipeViewModel
import com.rm.myrecipes.ui.utils.safeCollect
import com.rm.myrecipes.ui.utils.setGone
import com.rm.myrecipes.ui.utils.setVisible
import com.rm.myrecipes.ui.utils.showSystemUi
import com.rm.myrecipes.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment(), MenuProvider {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by viewModels()
    private val recipeAdapter by lazy { RecipesAdapter() }
    private lateinit var debouncingQueryTextListener: DebouncingQueryTextListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.fetchSafe(FetchType.Local)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        initRecyclerView()
        addObserver()

        safeCollect(viewModel.recipeResultState) {
            render(it)
        }

        binding.fabRecipes.setOnClickListener {
            val recipesBottomSheetFragment = RecipesBottomSheetFragment {
                onApplyButtonClicked()
            }
            recipesBottomSheetFragment.show(childFragmentManager, "bottom")
        }
    }

    private fun render(uiState: UiState<RecipeResult>) = with(binding) {
        when (uiState) {
            is UiState.Loading -> {
                progressBarRecipeFragment.setVisible()
            }
            is UiState.Success -> {
                progressBarRecipeFragment.setGone()
                recipeAdapter.recipeList = uiState.data.recipes
            }
            is UiState.Error -> {
                progressBarRecipeFragment.setGone()
                viewModel.fetchSafe(FetchType.Remote)
                requireContext().toast(uiState.message)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvRecipeFragment.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.recipe_fragment_menu, menu)
        initSearchView(menu)
    }

    private fun initSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        initializeSearch(searchView)
    }

    private fun initializeSearch(searchView: SearchView) {
        searchView.setOnSearchClickListener {
            searchView.maxWidth = Int.MAX_VALUE
            searchView.isSubmitButtonEnabled = true
        }

        searchView.setOnQueryTextListener(debouncingQueryTextListener)

        searchView.setOnCloseListener {
            searchView.onActionViewCollapsed()
            false
        }
    }

    private fun addObserver() {
        debouncingQueryTextListener = DebouncingQueryTextListener { newText ->
            newText?.let {
                if (it.isEmpty()) {
                    viewModel.fetchSafe(FetchType.Local)
                } else {
                    val fetchType = FetchType.Search
                    fetchType.data = it
                    viewModel.fetchSafe(fetchType)
                }
            }
        }
        lifecycle.addObserver(debouncingQueryTextListener)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    private fun onApplyButtonClicked() {
        binding.apply {
            ivNoConnection.setGone()
            txtNoConnection.setGone()
            progressBarRecipeFragment.setVisible()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
