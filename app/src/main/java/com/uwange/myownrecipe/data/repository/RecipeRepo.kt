package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.data.ResponseForm
import kotlinx.coroutines.flow.Flow

interface RecipeRepo {
    fun getRecipeList(foodId: Int): List<RecipeItem>
    fun observeRecipeDB(foodId: Int): Flow<List<RecipeItem>>

    fun getRecipe(recipeId: Int): RecipeItem?
    fun saveRecipeItem(recipeItem: RecipeItem): Flow<Exception?>
}