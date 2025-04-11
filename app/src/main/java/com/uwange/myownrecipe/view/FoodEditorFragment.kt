package com.uwange.myownrecipe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uwange.myownrecipe.databinding.FragmentFoodEditorBinding
import com.uwange.myownrecipe.viewModel.FoodEditorViewModel
import com.uwange.myownrecipe.viewModel.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

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
}