package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodItem
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
class RecipeDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo,
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<RecipeDetail>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<RecipeDetail>> = _uiState.asStateFlow()

    private val recipeArgumentData = savedStateHandle.getStateFlow("recipeArgumentData", RecipeArgumentData())

    private var foodItem: FoodItem? = null
    private var recipeItem: RecipeItem? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recipeArgumentData.collectLatest {
                _uiState.value = ResponseForm.Loading
                requestRecipeDetail(it.foodId, it.recipeId)
            }
        }
    }

    private fun requestRecipeDetail(foodId: Int, recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            foodItem = foodRepo.getFood(foodId)
            recipeItem = recipeRepo.getRecipe(recipeId)

            _uiState.value = when {
                foodItem == null ->
                    ResponseForm.Error("Not Found Food")
                recipeItem == null ->
                    ResponseForm.Error("Not Found Recipe")
                else ->
                    ResponseForm.Success
            }
        }
    }

    fun savedRecipeEditorArgumentData(recipeEditorArgumentData: RecipeArgumentData) {
        savedStateHandle["recipeEditorArgumentData"] = recipeEditorArgumentData
    }

    fun getFoodName(): String = foodItem?.foodName!!
    fun getRecipe(): RecipeItem? = recipeItem
}