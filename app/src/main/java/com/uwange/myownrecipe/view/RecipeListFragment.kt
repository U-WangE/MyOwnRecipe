package com.uwange.myownrecipe.view

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
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
    private companion object {
        private const val KEY_RECYCLER_STATE = "recycler_state"
        private const val FOOD_ARGUMENT_DATA = "foodArgumentData"
    }

    private var _binding: FragmentRecipeListBinding? = null
    private val binding: FragmentRecipeListBinding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = ::requireActivity)

    private val viewModel: RecipeListViewModel by viewModels()

    private lateinit var recipeItemAdapter: RecipeItemAdapter
    private var recyclerViewState: Parcelable? = null

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        recyclerViewState = binding.rvFoodList.layoutManager?.onSaveInstanceState()
        outState.putParcelable(KEY_RECYCLER_STATE, recyclerViewState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewState = savedInstanceState?.let {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                savedInstanceState.getParcelable(KEY_RECYCLER_STATE)
            else
                savedInstanceState.getParcelable(KEY_RECYCLER_STATE, Parcelable::class.java)
        }

        viewModel.setSavedStateData(mainViewModel.getData(FOOD_ARGUMENT_DATA) as FoodArgumentData)

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
                        recipeItemAdapter.submitList(itemList) {
                            recyclerViewState = recyclerViewState?.let {
                                binding.rvFoodList.layoutManager?.onRestoreInstanceState(it)
                                null
                            }
                        }

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
                mainViewModel.saveData(FOOD_ARGUMENT_DATA, it)
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
                mainViewModel.saveData(FOOD_ARGUMENT_DATA, it)
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