package com.uwange.myownrecipe.view

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
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
import com.uwange.myownrecipe.adapter.FoodItemAdapter
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.databinding.FragmentFoodListBinding
import com.uwange.myownrecipe.viewModel.FoodListViewModel
import com.uwange.myownrecipe.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodListFragment : Fragment() {
    private companion object {
        private const val KEY_RECYCLER_STATE = "recycler_state"
        private const val FOOD_ARGUMENT_DATA = "foodArgumentData"
    }

    private var _binding: FragmentFoodListBinding? = null
    private val binding: FragmentFoodListBinding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = ::requireActivity)
    private val viewModel: FoodListViewModel by viewModels()

    private lateinit var foodItemAdapter: FoodItemAdapter
    private var recyclerViewState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)

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

        setupFoodRecyclerView()

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
                        foodItemAdapter.submitList(itemList) {
                            recyclerViewState = recyclerViewState?.let {
                                binding.rvFoodList.layoutManager?.onRestoreInstanceState(it)
                                null
                            }
                        }

                        clickListener()
                    }
                }
            }
        }
    }

    private fun setupFoodRecyclerView() {
        foodItemAdapter = FoodItemAdapter { foodId, foodName ->
            // Food Item Click Callback
            Log.i(this::class.simpleName, "Food Item Clicked: foodId : $foodId, foodName : $foodName")
            mainViewModel.saveData(FOOD_ARGUMENT_DATA, FoodArgumentData(foodId, null, foodName))

            findNavController().navigate(
                FoodListFragmentDirections.actionFoodListFragmentToRecipeListFragment()
            )
        }
        binding.rvFoodList.adapter = foodItemAdapter
    }

    private fun clickListener() {
        binding.ivAddFood.setOnClickListener {
            findNavController().navigate(
                FoodListFragmentDirections.actionFoodListFragmentToFoodEditorFragment()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}