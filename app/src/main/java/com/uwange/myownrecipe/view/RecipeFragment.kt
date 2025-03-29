package com.uwange.myownrecipe.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.uwange.myownrecipe.databinding.FragmentRecipeBinding
import com.uwange.myownrecipe.view.FoodFragmentDirections.ActionFoodFragmentToRecipeFragment

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding: FragmentRecipeBinding get() = _binding!!

    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)

        Log.d("여기", args.foodArgumentData.toString())
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}