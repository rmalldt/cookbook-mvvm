package com.rm.myrecipes.ui.fragments.foodtrivia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FragmentFoodTriviaBinding
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.foodtrivia.viewmodel.FoodTriviaViewModel
import com.rm.myrecipes.ui.utils.safeCollect
import com.rm.myrecipes.ui.utils.setGone
import com.rm.myrecipes.ui.utils.setVisible
import com.rm.myrecipes.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodTriviaFragment : Fragment() {

    private var _binding: FragmentFoodTriviaBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FoodTriviaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodTriviaBinding.inflate(inflater, container, false)
        viewModel.safeCall()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        safeCollect(viewModel.foodTriviaState) {
            render(it)
        }

        setMenu()
    }

    private fun render(uiState: UiState<FoodTrivia>) {
        when (uiState) {
            is UiState.Loading -> {
                with(binding) {
                    progressBar.setVisible()
                    ivError.setGone()
                    tvError.setGone()
                    tvFoodTrivia.setGone()
                }
            }

            is UiState.Success -> {
                with(binding) {
                    progressBar.setGone()
                    ivError.setGone()
                    tvError.setGone()
                    tvFoodTrivia.setVisible()
                    tvFoodTrivia.text = uiState.data.trivia
                }
            }

            is UiState.Error -> {
                with(binding) {
                    progressBar.setGone()
                    ivError.setVisible()
                    tvError.setVisible()
                    tvFoodTrivia.setGone()
                    requireContext().toast(uiState.message)
                }
            }
        }
    }

    private fun setMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.trivia_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_item_share_trivia) {
                    val foodTrivia: String = binding.tvFoodTrivia.text.toString()
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, foodTrivia)
                        type = "text/plain"
                    }
                    startActivity(shareIntent)
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}