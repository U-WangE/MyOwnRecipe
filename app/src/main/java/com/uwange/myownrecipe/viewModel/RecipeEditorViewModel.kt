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
class RecipeEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo,
    private val foodRepo: FoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow<ResponseForm<RecipeDetail>>(ResponseForm.Loading)
    val uiState: StateFlow<ResponseForm<RecipeDetail>> = _uiState.asStateFlow()
    private val _saveState = MutableStateFlow<ResponseForm<Int>>(ResponseForm.Loading)
    val saveState: StateFlow<ResponseForm<Int>> = _saveState.asStateFlow()

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
                _uiState.value = ResponseForm.Error("Not Found Food ID")
            else {
                foodItem = foodRepo.getFood(foodId)
                foodItem?.let {
                    recipeItem =
                        if (recipeId != -1) recipeRepo.getRecipe(recipeId) else RecipeItem(foodId = foodId)

                    _uiState.value = ResponseForm.Success

                }?: let { _uiState.value = ResponseForm.Error("Not Found Food") }
            }

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
        viewModelScope.launch(Dispatchers.IO) {
            if (recipeName.isBlank()) {
                _saveState.value = ResponseForm.Error("Recipe Name is Blank")
                return@launch
            }

            recipeRepo.saveRecipeItem(
                recipeItem?.apply {
                    this.recipeName = recipeName
                    this.bookmark = bookmark
                    this.score = score
                    this.ingredients = ingredients
                    this.recipeSteps = recipeSteps
                    this.recipeReview = recipeReview
                } ?: let {
                    RecipeItem(
                        foodId = recipeItem?.foodId!!,
                        recipeName = recipeName,
                        imageUrl = "",
                        imageDescription = "",
                        bookmark = bookmark,
                        score = score,
                        ingredients = ingredients,
                        recipeSteps = recipeSteps,
                        recipeReview = recipeReview
                    )
                }
            ).collect {
                when(it) {
                    null -> _saveState.value = ResponseForm.Success
                    else -> _saveState.value = ResponseForm.Error(it.message!!)
                }
            }
        }
    }

    fun getFoodName(): String = foodItem?.foodName!!
    fun getRecipe(): RecipeItem? = recipeItem

    fun savedRecipeArgumentData() {
        savedStateHandle["recipeArgumentData"] = RecipeArgumentData(recipeItem?.recipeId!!, recipeItem?.foodId!!)
    }

}