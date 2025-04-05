package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.RecipeDetail
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.data.repository.RecipeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo
): ViewModel() {

    private val _uiState = MutableStateFlow<ResponseForm<RecipeDetail>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<RecipeDetail>> = _uiState.asStateFlow()

    private val recipeArgumentData = savedStateHandle.getStateFlow("recipeArgumentData", RecipeArgumentData())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recipeArgumentData.collectLatest {
                _uiState.value = ResponseForm.Loading
                requestRecipeDetail(it.recipeId)
            }
        }
    }

    private fun requestRecipeDetail(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
//        recipeRepo.requestRecipeDetail(recipeId)

        }
    }
}