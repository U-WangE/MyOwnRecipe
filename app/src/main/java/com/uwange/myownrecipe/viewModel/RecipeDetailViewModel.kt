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
class RecipeDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo
): ViewModel() {
    private var mainViewModel: MainViewModel? = null

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<Exception?>(null)
    val isError: StateFlow<Exception?> = _isError.asStateFlow()

    private val _recipeItem = MutableStateFlow<RecipeItem>(RecipeItem())
    val recipeItem: StateFlow<RecipeItem> = _recipeItem.asStateFlow()

    private val foodArgumentData = savedStateHandle.getStateFlow("foodArgumentData", FoodArgumentData())
    private var restoreRecipeItem = savedStateHandle.get<RecipeItem>("recipeItem")

    init {
        viewModelScope.launch {
            foodArgumentData.collectLatest {
                if (restoreRecipeItem != null &&
                    it.foodId == restoreRecipeItem?.foodId &&
                    it.recipeId == restoreRecipeItem?.recipeId)
                    _recipeItem.value = restoreRecipeItem!!

                fetchData()
            }
        }
    }

    fun getMainViewModel(mainViewModel: MainViewModel) {
        this.mainViewModel = mainViewModel
    }

    fun getMainArgumentData() {
        mainViewModel?.getData("foodArgumentData")?.let {
            savedStateHandle["foodArgumentData"] = it as FoodArgumentData
        }
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                when (val recipe = recipeRepo.getRecipe(foodArgumentData.value.recipeId!!)) {
                    is ResponseForm.Success -> {
                        setRecipeItem(recipe.data!!)
                    }

                    is ResponseForm.Error -> {
                        _isError.value = recipe.exception
                    }
                }
            } catch (e: Exception) {
                _isError.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setRecipeItem(recipeItem: RecipeItem) {
        savedStateHandle["recipeItem"] = recipeItem
        this.restoreRecipeItem = recipeItem
        _recipeItem.value = recipeItem
    }

    fun getFoodName(): String = foodArgumentData.value.foodName

    fun saveFoodArgumentData() {
        val foodArgumentData = foodArgumentData.value.copy(recipeId = recipeItem.value.recipeId)
        savedStateHandle["foodArgumentData"] = foodArgumentData
    }
}