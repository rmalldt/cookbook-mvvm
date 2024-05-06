package com.rm.myrecipes.ui.fragments.details.instructions

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.rm.myrecipes.databinding.FragmentInstructionsBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.utils.extension.toast

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
    }

    private fun initWebView() {
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("recipe", Recipe::class.java)
        } else {
            arguments?.getParcelable("recipe")
        }

        with(binding.webViewInstruction) {
            webViewClient = object :WebViewClient(){}
            setInitialScale(initialScale())

            if (recipe != null) {
                loadUrl(recipe.sourceUrl)
            } else {
                requireContext().toast("Cannot load the Web Page, please try again")
            }
        }
    }

    private fun initialScale(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val width = windowMetrics.bounds.width()
            width/360
        } else {
            val display = ((requireActivity().windowManager) as WindowManager).defaultDisplay
            val width = display.width
            width/360
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
