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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentRecipesBinding
import com.rm.myrecipes.domain.data.RecipeResult
import com.rm.myrecipes.ui.common.FetchState
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

    private lateinit var viewModel: RecipeViewModel
    private lateinit var debouncingQueryTextListener: DebouncingQueryTextListener

    private val recipeAdapter by lazy { RecipesAdapter() }

    private val args by navArgs<RecipesFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        if (savedInstanceState == null && !args.applyChips) {
            viewModel.fetchSafe()
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

        showStatusActionBarAndNavigationView()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        initRecyclerView()
        addObserver()

        safeCollect(viewModel.recipeResultState) {
            render(it)
        }

        binding.fabRecipes.setOnClickListener {
            findNavController().navigate(
                RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheetFragment()
            )
        }
    }

    private fun render(uiState: UiState<RecipeResult>) = with(binding) {
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
                    viewModel.fetchSafe(FetchState.FetchRemote)
                }

            }
            is UiState.Error -> {
                requireContext().toast(uiState.message)
                viewModel.fetchSafe(FetchState.FetchLocal)
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

    private fun showStatusActionBarAndNavigationView() {
        requireActivity().showSystemUi()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisible()
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
                    viewModel.fetchSafe(FetchState.FetchLocal)
                } else {
                    val fetchState = FetchState.FetchSearch
                    fetchState.data = it
                    viewModel.fetchSafe(fetchState)
                }
            }
        }
        lifecycle.addObserver(debouncingQueryTextListener)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
