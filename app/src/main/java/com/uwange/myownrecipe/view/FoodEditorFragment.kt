package com.uwange.myownrecipe.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.databinding.FragmentFoodEditorBinding
import com.uwange.myownrecipe.viewModel.FoodEditorViewModel
import com.uwange.myownrecipe.viewModel.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodEditorFragment : Fragment() {
    private var _binding: FragmentFoodEditorBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FoodEditorViewModel

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodEditorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFoodEditorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSaveBtn.setOnClickListener {
            viewModel.saveNewFood(binding.etFoodName.text.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ResponseForm.Loading -> {

                        }
                        is ResponseForm.Success -> {

                        }
                        is ResponseForm.Error -> {

                        }
                    }
                }
            }
        }
    }
}