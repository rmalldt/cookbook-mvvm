package com.rm.myrecipes.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rm.myrecipes.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashScreenDuration()
    }

    private fun splashScreenDuration() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRecipesFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
