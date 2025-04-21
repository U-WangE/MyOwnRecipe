package com.uwange.myownrecipe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.uwange.myownrecipe.adapter.RecipeItemAdapter
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.databinding.FragmentRecipeListBinding
import com.uwange.myownrecipe.viewModel.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding: FragmentRecipeListBinding get() = _binding!!
    private lateinit var viewModel: RecipeListViewModel

    private lateinit var recipeItemAdapter: RecipeItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[RecipeListViewModel::class.java]
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

        setupRecipeRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ResponseForm.Loading ->{

                        }
                        is ResponseForm.Success -> {
                            recipeItemAdapter.submitList(viewModel.getRecipeList())

                            clickListener()
                        }
                        is ResponseForm.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun setupRecipeRecyclerView() {
        recipeItemAdapter = RecipeItemAdapter { recipeId, foodId ->
            // Recipe Item Click Callback

            viewModel.savedRecipeArgumentData(RecipeArgumentData(recipeId = recipeId, foodId = foodId))

            findNavController().navigate(
                RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment()
            )
        }

        binding.rvFoodList.adapter = recipeItemAdapter
    }

    private fun clickListener() {
        binding.ivAddRecipe.setOnClickListener {
            viewModel.savedRecipeArgumentData(RecipeArgumentData(recipeId = null, foodId = viewModel.getFoodId()))

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