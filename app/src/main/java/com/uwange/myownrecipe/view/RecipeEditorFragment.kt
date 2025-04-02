package com.uwange.myownrecipe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uwange.myownrecipe.R
import com.uwange.myownrecipe.databinding.FragmentRecipeEditorBinding

class RecipeEditorFragment : Fragment() {
    private var _binding: FragmentRecipeEditorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecipeEditorBinding.inflate(inflater, container, false)

        return binding.root
    }
}