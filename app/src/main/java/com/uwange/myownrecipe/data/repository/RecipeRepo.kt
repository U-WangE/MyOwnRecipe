package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.RecipeItem
import kotlinx.coroutines.flow.Flow

interface RecipeRepo {
    fun getRecipeList(foodId: Int): List<RecipeItem>
    fun observeRecipeDB(foodId: Int): Flow<List<RecipeItem>>

    fun getRecipe(recipeId: Int): RecipeItem?
}