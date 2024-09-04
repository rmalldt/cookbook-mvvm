package com.rm.myrecipes.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rm.myrecipes.data.SelectedChipPreferences
import com.rm.myrecipes.data.common.ApiConstants.Companion.DEFAULT_DIET_TYPE
import com.rm.myrecipes.data.common.ApiConstants.Companion.DEFAULT_MEAL_TYPE
import com.rm.myrecipes.databinding.FragmentRecipesBottomSheetBinding
import com.rm.myrecipes.ui.common.FetchType
import com.rm.myrecipes.ui.common.UiState
import com.rm.myrecipes.ui.fragments.recipes.viewmodel.RecipeViewModel
import com.rm.myrecipes.ui.utils.extension.safeCollect
import timber.log.Timber

class RecipesBottomSheetFragment(
    val onApplyClicked: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        safeCollect(viewModel.selectedChipState) {
            render(it)
        }

        applyChipState()
    }

    private fun render(uiState: UiState<SelectedChipPreferences>) {
        when (uiState) {
            is UiState.Loading -> {}

            is UiState.Success -> {
                updateChipState(uiState.data.selectedMealId, binding.chipGroupMealType)
                updateChipState(uiState.data.selectedDietId, binding.chipGroupDietType)
            }

            is UiState.Error -> {}
        }
    }

    private fun updateChipState(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Timber.d("Recipe: ChipGroup find view by id threw: ${e.message.toString()}")
            }
        }
    }

    private fun applyChipState()  = with(binding) {
        var selectedMealType = DEFAULT_MEAL_TYPE
        var selectedMealId = 0
        var selectedDietType = DEFAULT_DIET_TYPE
        var selectedDietId = 0

        chipGroupMealType.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedMealId = checkedIds.first()
            val chip = group.findViewById<Chip>(selectedMealId)
            selectedMealType = chip.text.toString().lowercase()
        }

        chipGroupDietType.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedDietId = checkedIds.first()
            val chip = group.findViewById<Chip>(selectedDietId)
            selectedDietType = chip.text.toString().lowercase()
        }

        btnApply.setOnClickListener {
            onApplyClicked()
            viewModel.applyMealDietType(
                selectedMealType,
                selectedMealId,
                selectedDietType,
                selectedDietId
            )
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
