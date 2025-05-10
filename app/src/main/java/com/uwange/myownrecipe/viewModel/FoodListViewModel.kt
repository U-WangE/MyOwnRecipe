package com.uwange.myownrecipe.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.data.repository.FoodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<Exception?>(null)
    val isError: StateFlow<Exception?> = _isError.asStateFlow()

    private val _itemList = MutableStateFlow<List<FoodItem>>(emptyList())
    val itemList: StateFlow<List<FoodItem>> = _itemList.asStateFlow()

    private var restoreFoodList: List<FoodItem> = savedStateHandle.get<List<FoodItem>>("foodList")?: emptyList()

    init {
        if (restoreFoodList.isNotEmpty()) {
            _itemList.value = restoreFoodList
        }

        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            foodRepo.observeFoodDB()
                .onCompletion { _isLoading.value = false }
                .catch { _isError.value = Exception(it) }
                .collectLatest { foods ->
                    _isLoading.value = true
                    when (foods) {
                        is ResponseForm.Success -> {
                            if (restoreFoodList != foods.data) {
                                savedStateHandle["foodList"] = restoreFoodList
                                _itemList.value = foods.data!!
                            }
                        }
                        is ResponseForm.Error -> {
                            _isError.value = foods.exception
                        }
                    }
                }
        }
    }
}