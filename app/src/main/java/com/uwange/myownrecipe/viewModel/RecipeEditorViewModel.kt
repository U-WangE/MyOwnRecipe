package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.RecipeArgumentData
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
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo,
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<RecipeItem>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<RecipeItem>> = _uiState.asStateFlow()
    private val _saveState = MutableStateFlow<ResponseForm<Long>>(ResponseForm.Loading)
    val saveState: StateFlow<ResponseForm<Long>> = _saveState.asStateFlow()

    private val recipeEditorArgumentData = savedStateHandle.getStateFlow("recipeEditorArgumentData", RecipeArgumentData())

    private var foodItem: FoodItem? = null
    private var recipeItem: RecipeItem? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            recipeEditorArgumentData.collectLatest {
                _uiState.value = ResponseForm.Loading

                requestRecipeDetail(it.recipeId, it.foodId)
            }
        }
    }

    private fun requestRecipeDetail(recipeId: Int, foodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (foodId == -1)
                _uiState.value = ResponseForm.Error(Exception("Not Found Food ID"))
            else {
                when(val food = foodRepo.getFood(foodId)) {
                    is ResponseForm.Success -> {
                        foodItem = food.data?.copy()

                        if (recipeId == -1) {
                            recipeItem = RecipeItem(foodId = foodId)
                            _uiState.value = ResponseForm.Success(recipeItem!!)
                        } else
                            setRecipeForDB(recipeId)
                    }
                    is ResponseForm.Error -> {
                        _uiState.value = ResponseForm.Error(food.exception)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setRecipeForDB(recipeId: Int) {
        when (val recipeItem = recipeRepo.getRecipe(recipeId)) {
            is ResponseForm.Success -> {
                this.recipeItem = recipeItem.data?.copy()
                _uiState.value = ResponseForm.Success(this.recipeItem!!)
            }
            is ResponseForm.Error -> {
                _uiState.value = ResponseForm.Error(recipeItem.exception)
            }
            else -> {}
        }
    }

    fun saveRecipeItem(
        recipeName: String,
        bookmark: Boolean,
        score: String,
        ingredients: String,
        recipeSteps: String,
        recipeReview: String
    ) {
        if (recipeName.isBlank() || recipeItem == null) {
            _saveState.value = ResponseForm.Error(Exception("Recipe Name is Blank"))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val recipeItem = recipeItem!!.copy()
            recipeItem.apply {
                this.recipeName = recipeName
                this.bookmark = bookmark
                this.score = score
                this.ingredients = ingredients
                this.recipeSteps = recipeSteps
                this.recipeReview = recipeReview
            }

            when (val rowId = recipeRepo.saveRecipeItem(recipeItem)) {
                is ResponseForm.Success -> {
                    _saveState.value = ResponseForm.Success(rowId.data!!)
                }
                is ResponseForm.Error -> {
                    _saveState.value = ResponseForm.Error(rowId.exception)
                }
                else -> {}
            }
        }
    }

    fun getFoodName(): String = foodItem?.foodName!!
    fun getRecipeItem(): RecipeItem? = recipeItem

    fun savedRecipeArgumentData() {
        savedStateHandle["recipeArgumentData"] = RecipeArgumentData(recipeItem?.recipeId!!, recipeItem?.foodId!!)
    }

}