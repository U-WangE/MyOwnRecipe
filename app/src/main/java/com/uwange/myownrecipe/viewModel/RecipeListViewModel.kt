package com.uwange.myownrecipe.viewModel

import android.util.Log
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
    private var foodId: Int? = null
    private var recipeList: List<RecipeItem>

    init {
        recipeList = savedStateHandle.get<List<RecipeItem>>("recipeList") ?: emptyList()

        // Recipe List 화면 init 시 foodId 값 확인
        viewModelScope.launch(Dispatchers.IO) {
            foodArgumentData.collectLatest { argFood ->
                _uiState.value = ResponseForm.Loading

                if (foodId != argFood.foodId)
                    foodId = argFood.foodId!!

                requestRecipeList(foodId!!)
            }
        }

        // Recipe List 확인용
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepo.observeRecipeDB(foodId).collectLatest { recipes ->
                _uiState.value = ResponseForm.Loading
                if (recipeList != recipes.data!!) {
                    setRecipeList(recipes.data)
                }
                _uiState.value = ResponseForm.Success(recipeList)
            }
        }
    }

    private fun requestRecipeList(foodId: Int) {
        when (val recipes = recipeRepo.getRecipeList(foodId)) {
            is ResponseForm.Success -> {
                if (recipeList != recipes.data!!)
                    setRecipeList(recipes.data)
                else
                    _uiState.value = ResponseForm.Success(recipeList)
            }
            is ResponseForm.Error -> {
                _uiState.value = ResponseForm.Error(recipes.exception)
            }
            else -> {}
        }
    }

    private fun setRecipeList(recipeList: List<RecipeItem>) {
        savedStateHandle["recipeList"] = recipeList
        this.recipeList = recipeList
        _uiState.value = ResponseForm.Success(recipeList)
    }

    fun getRecipeList(): List<RecipeItem> = recipeList

    fun savedRecipeArgumentData(recipeArgumentData: RecipeArgumentData) {
        savedStateHandle["recipeArgumentData"] = recipeArgumentData
    }

    fun getFoodId(): Int = foodId!!
}