package com.uwange.myownrecipe.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.RecipeDetail
import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.data.repository.FoodRepo
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
class RecipeEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo,
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<RecipeDetail>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<RecipeDetail>> = _uiState.asStateFlow()

    private val recipeEditorArgumentData = savedStateHandle.getStateFlow("recipeEditorArgumentData", RecipeArgumentData())

    private var recipeItem: RecipeItem? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recipeEditorArgumentData.collectLatest {
                Log.d("여기", it.toString())
                _uiState.value = ResponseForm.Loading
                requestRecipeDetail(it.recipeId)
            }
        }
    }

    private fun requestRecipeDetail(recipeId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (recipeId != -1 && recipeId != null) {
                recipeItem = recipeRepo.getRecipe(recipeId)
            }
            _uiState.value = ResponseForm.Success
        }
    }

    fun getFoodName(): String = recipeEditorArgumentData.value.foodName
    fun getRecipe(): RecipeItem? = recipeItem

    fun foodCategoryList(): List<FoodArgumentData> = foodRepo.getFoodCategoryList()
}