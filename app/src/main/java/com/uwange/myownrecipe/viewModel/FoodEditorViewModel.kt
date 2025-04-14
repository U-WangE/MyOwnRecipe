package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodArgumentData
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
class FoodEditorViewModel @Inject constructor(
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<Long>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<Long>> = _uiState.asStateFlow()

    fun saveNewFood(foodName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ResponseForm.Loading
            if (!isFoodNameDuplicated(foodName)) {
                when (val rowId = foodRepo.saveFoodItem(foodName)) {
                    is ResponseForm.Success -> {
                        _uiState.value = ResponseForm.Success(rowId.data!!)
                    }
                    is ResponseForm.Error -> {
                        _uiState.value = ResponseForm.Error(rowId.exception)
                    }
                    else -> {}
                }
            } else {
                _uiState.value = ResponseForm.Error(Exception("Already Exist"))
            }
        }
    }

    private fun isFoodNameDuplicated(foodName: String): Boolean {
        return foodRepo.isFoodNameAlreadyExist(foodName)
    }
}