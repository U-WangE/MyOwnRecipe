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
import com.uwange.myownrecipe.adapter.FoodItemAdapter
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.databinding.FragmentFoodListBinding
import com.uwange.myownrecipe.viewModel.FoodListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodListFragment : Fragment() {
    private var _binding: FragmentFoodListBinding? = null
    private val binding: FragmentFoodListBinding get() = _binding!!
    private lateinit var viewModel: FoodListViewModel

    private lateinit var foodItemAdapter: FoodItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFoodRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ResponseForm.Loading ->{

                        }
                        is ResponseForm.Success -> {
                            foodItemAdapter.submitList(state.data)
                        }
                        is ResponseForm.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun setupFoodRecyclerView() {
        foodItemAdapter = FoodItemAdapter { foodId, foodName ->
            // Food Item Click Callback
            findNavController().navigate(
                FoodListFragmentDirections.actionFoodListFragmentToRecipeListFragment(
                    FoodArgumentData(
                        foodId = foodId,
                        name = foodName
                    )
                )
            )
        }
        binding.rvFoodList.adapter = foodItemAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}