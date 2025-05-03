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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepo: RecipeRepo
): ViewModel() {
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<Exception?>(null)
    val isError: StateFlow<Exception?> = _isError.asStateFlow()

    private val _itemList = MutableStateFlow<List<RecipeItem>>(emptyList())
    val itemList: StateFlow<List<RecipeItem>> = _itemList.asStateFlow()

    private var foodArgumentData = savedStateHandle.getStateFlow("foodArgumentData", FoodArgumentData())
    private var restoreRecipeList: List<RecipeItem> = savedStateHandle.get<List<RecipeItem>>("recipeList") ?: emptyList()

    init {
        if (restoreRecipeList.isNotEmpty() &&
            foodArgumentData.value.foodId == restoreRecipeList[0].foodId)
            _itemList.value = restoreRecipeList

        fetchData()
    }

    fun setSavedStateData(foodArgumentData: FoodArgumentData) {
        savedStateHandle["foodArgumentData"] = foodArgumentData
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepo.observeRecipeDB(foodArgumentData.value.foodId)
                .onCompletion { _isLoading.value = false }
                .catch { _isError.value = Exception(it) }
                .collectLatest { recipes ->
                    _isLoading.value = true
                    when (recipes) {
                        is ResponseForm.Success -> {
                            if (restoreRecipeList != recipes.data!!)
                                setRecipeList(recipes.data)
                        }
                        is ResponseForm.Error -> {
                            _isError.value = recipes.exception
                        }
                    }
                }
        }
    }

    private fun setRecipeList(recipeList: List<RecipeItem>) {
        savedStateHandle["recipeList"] = recipeList
        this.restoreRecipeList = recipeList
        _itemList.value = recipeList
    }

    fun getFoodName(): String = foodArgumentData.value.foodName

    fun saveFoodArgumentData(recipeId: Int? = null): FoodArgumentData {
        return foodArgumentData.value.copy(recipeId = recipeId)
    }
}