package com.uwange.myownrecipe.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.uwange.myownrecipe.adapter.RecipeItemAdapter
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.databinding.FragmentRecipeListBinding
import com.uwange.myownrecipe.viewModel.MainViewModel
import com.uwange.myownrecipe.viewModel.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding: FragmentRecipeListBinding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = ::requireActivity)

    private val viewModel: RecipeListViewModel by viewModels()

    private lateinit var recipeItemAdapter: RecipeItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setSavedStateData(mainViewModel.getData("foodArgumentData") as FoodArgumentData)

        setupRecipeRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect {
                        // TODO:: 로딩 처리
                    }
                }
                launch {
                    viewModel.isError.collect {
                        // TODO:: Error 처리
                    }
                }
                launch {
                    viewModel.itemList.collect { itemList ->
                        recipeItemAdapter.submitList(itemList)

                        binding.tvFoodName.text = viewModel.getFoodName()

                        clickListener()
                    }
                }
            }
        }
    }

    private fun setupRecipeRecyclerView() {
        recipeItemAdapter = RecipeItemAdapter { recipeId ->

            viewModel.saveFoodArgumentData(recipeId).let {
                mainViewModel.saveData("foodArgumentData", it)
            }

            findNavController().navigate(
                RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment()
            )
        }

        binding.rvFoodList.adapter = recipeItemAdapter
    }

    private fun clickListener() {
        binding.ivAddRecipe.setOnClickListener {
            viewModel.saveFoodArgumentData().let {
                mainViewModel.saveData("foodArgumentData", it)
            }

            findNavController().navigate(
                RecipeListFragmentDirections.actionRecipeListFragmentToRecipeEditorFragment()
            )
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}