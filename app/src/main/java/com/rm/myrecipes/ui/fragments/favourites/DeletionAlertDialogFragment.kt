package com.rm.myrecipes.ui.fragments.favourites

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.AlertDilalogLayoutBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.fragments.favourites.viewmodel.FavouriteRecipesViewModel
import com.rm.myrecipes.ui.utils.snackBar

class DeletionAlertDialogFragment : DialogFragment() {

    private var _binding: AlertDilalogLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteRecipesViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlertDilalogLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnDialogCancel.setOnClickListener {
            dismiss()
        }

        binding.btnDialogDelete.setOnClickListener {
            val selectedRecipes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelableArrayList("Dialog", Recipe::class.java)
            } else {
                arguments?.getParcelableArrayList("Dialog")
            }
            selectedRecipes?.forEach { viewModel.deleteFavouriteRecipe(it) }

            requireParentFragment().view?.snackBar("Deleted ${selectedRecipes?.size} recipe(s)")
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance(bundle: Bundle): DeletionAlertDialogFragment {
            val dialogFragment = DeletionAlertDialogFragment()
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }
}