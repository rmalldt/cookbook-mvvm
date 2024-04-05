package com.rm.myrecipes.ui.fragments.details.ingredients

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.myrecipes.databinding.FragmentIngredientsBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.fragments.details.ingredients.adapter.IngredientsAdapter

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val ingredientsAdapter by lazy {
        IngredientsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("recipe", Recipe::class.java)
        } else {
            arguments?.getParcelable("recipe")
        }

        recipe?.let {
            ingredientsAdapter.ingredientList = it.extendedIngredients
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}