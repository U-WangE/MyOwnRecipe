package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.RecipeItem
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
class RecipeListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<List<RecipeItem>>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<List<RecipeItem>>> = _uiState.asStateFlow()

    private val foodArgumentData = savedStateHandle.getStateFlow("foodArgumentData", FoodArgumentData())
    private var foodId: Int = -1
    private var recipeList: List<RecipeItem>

    init {
        recipeList = savedStateHandle.get<List<RecipeItem>>("recipeList") ?: emptyList()

        viewModelScope.launch(Dispatchers.IO) {
            foodArgumentData.collectLatest {
                _uiState.value = ResponseForm.Loading

                if (it.foodId == -1) {
                    _uiState.value = ResponseForm.Error("Not Found Recipe")
                } else {
                    foodId = it.foodId
                    setRecipeList(requestRecipeList(it.foodId))
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            recipeRepo.observeRecipeDB(foodId).collectLatest { recipes ->
                _uiState.value = ResponseForm.Loading
                if (recipeList != recipes) {
                    setRecipeList(recipes)
                }
                _uiState.value = ResponseForm.Success
            }
        }
    }

    private fun requestRecipeList(foodId: Int) = recipeRepo.getRecipeList(foodId)

    private fun setRecipeList(recipeList: List<RecipeItem>) {
        savedStateHandle["recipeList"] = recipeList
        this.recipeList = recipeList
        _uiState.value = ResponseForm.Success
    }

    fun getRecipeList(): List<RecipeItem> = recipeList

    fun savedRecipeArgumentData(recipeArgumentData: RecipeArgumentData) {
        savedStateHandle["recipeArgumentData"] = recipeArgumentData
    }

    fun getFoodId(): Int = foodId!!
}