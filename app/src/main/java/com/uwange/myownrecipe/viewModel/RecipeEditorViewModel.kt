package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwange.myownrecipe.data.FoodArgumentData
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
class RecipeEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo
): ViewModel() {
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<Exception?>(null)
    val isError: StateFlow<Exception?> = _isError.asStateFlow()

    private val _recipeItem = MutableStateFlow<RecipeItem>(RecipeItem())
    val recipeItem: StateFlow<RecipeItem> = _recipeItem.asStateFlow()

    private val _saveState = MutableStateFlow<Boolean>(false)
    val saveState: StateFlow<Boolean> = _saveState.asStateFlow()

    private var foodArgumentData = savedStateHandle.getStateFlow("foodArgumentData", FoodArgumentData())
    private var restoreRecipeEditData = savedStateHandle.get<RecipeItem>("recipeEditData")

    init {
        viewModelScope.launch {
            foodArgumentData.collectLatest {
                if (restoreRecipeEditData != null &&
                    it.foodId == restoreRecipeEditData?.foodId &&
                    it.recipeId == restoreRecipeEditData?.recipeId)
                    _recipeItem.value = restoreRecipeEditData!!
                else
                    fetchData(it)
            }
        }
    }

    fun setSavedStateData(foodArgumentData: FoodArgumentData) {
        savedStateHandle["foodArgumentData"] = foodArgumentData
    }

    private fun fetchData(foodArgumentData: FoodArgumentData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true

                foodArgumentData.recipeId?.let {
                    when (val recipeItem = recipeRepo.getRecipe(it)) {
                        is ResponseForm.Success -> {
                            setRecipeItem(recipeItem.data!!)
                        }
                        is ResponseForm.Error -> {
                            _isError.value = recipeItem.exception
                        }
                    }
                }?: run { _recipeItem.value = RecipeItem() }
            } catch (e: Exception) {
                _isError.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setRecipeItem(recipeItem: RecipeItem) {
        savedStateHandle["recipeEditData"] = recipeItem
        this.restoreRecipeEditData = recipeItem
        _recipeItem.value = recipeItem
    }



    fun saveRecipeItem(
        recipeName: String,
        bookmark: Boolean,
        score: String,
        ingredients: String,
        recipeSteps: String,
        recipeReview: String
    ) {
        if (recipeName.isBlank()) {
            _isError.value = Exception("Recipe Name is Blank")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val recipeItem = recipeItem.value.copy(
                recipeName = recipeName,
                bookmark = bookmark,
                score = score,
                ingredients = ingredients,
                recipeSteps = recipeSteps,
                recipeReview = recipeReview
            )

            when (val rowId = recipeRepo.saveRecipeItem(recipeItem)) {
                is ResponseForm.Success -> {
                    _saveState.value = true
                }
                is ResponseForm.Error -> {
                    _isError.value = rowId.exception
                }
            }
        }
    }

    fun getFoodName(): String = foodArgumentData.value.foodName
}