package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.data.repository.FoodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<List<FoodItem>>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<List<FoodItem>>> = _uiState.asStateFlow()

    private var foodList: List<FoodItem>

    init {
        foodList = savedStateHandle.get<List<FoodItem>>("foodList")?: emptyList()

        viewModelScope.launch(Dispatchers.IO) {
            foodRepo.observeFoodDB().collectLatest { foods ->
                _uiState.value = ResponseForm.Loading
                if (foodList != foods) {
                    savedStateHandle["foodList"] = foods
                    setFoodList(foods)
                }
                _uiState.value = ResponseForm.Success
            }
        }
    }

    private fun setFoodList(foodList: List<FoodItem>) {
        this.foodList = foodList
    }

    fun getFoodList(): List<FoodItem> = foodList
}