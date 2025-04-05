package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.RecipeDetail
import com.uwange.myownrecipe.data.ResponseForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow<ResponseForm<RecipeDetail>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<RecipeDetail>> = _uiState.asStateFlow()

    private val recipeArgumentData = savedStateHandle.getStateFlow("recipeArgumentData", RecipeArgumentData())

    init {
        viewModelScope.launch {
            recipeArgumentData.collectLatest {
                _uiState.value = ResponseForm.Loading
                // TODO:: api 정의 완료 시 처리 필요
//                val recipe = repository.getRecipeDetail(it.recipeId)
                _uiState.value = ResponseForm.Success
            }
        }
    }
}