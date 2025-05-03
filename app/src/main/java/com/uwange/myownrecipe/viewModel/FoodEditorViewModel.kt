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
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<Exception?>(null)
    val isError: StateFlow<Exception?> = _isError.asStateFlow()

    private val _saveState = MutableStateFlow<Boolean>(false)
    val saveState: StateFlow<Boolean> = _saveState.asStateFlow()

    fun saveNewFood(foodName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            if (!isFoodNameDuplicated(foodName)) {
                when (val rowId = foodRepo.saveFoodItem(foodName)) {
                    is ResponseForm.Success -> {
                        _saveState.value = true
                    }
                    is ResponseForm.Error -> {
                        _isError.value = rowId.exception
                    }
                }
            }

            _isLoading.value = false
        }
    }

    private fun isFoodNameDuplicated(foodName: String): Boolean {
        return foodRepo.isFoodNameAlreadyExist(foodName)
    }
}