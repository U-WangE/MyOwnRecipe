package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.data.ResponseForm
import kotlinx.coroutines.flow.Flow

interface RecipeRepo {
    fun getRecipeList(foodId: Int): ResponseForm<List<RecipeItem>>
    fun observeRecipeDB(foodId: Int): Flow<ResponseForm<List<RecipeItem>>>

    fun getRecipe(recipeId: Int): ResponseForm<RecipeItem>
    fun saveRecipeItem(recipeItem: RecipeItem): ResponseForm<Long>
}