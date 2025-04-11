package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.data.dao.RecipeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeRepoImpl @Inject constructor(
    private val recipeDao: RecipeDao
): RecipeRepo {
    override fun getRecipeList(foodId: Int): List<RecipeItem> =
        recipeDao.getRecipesByFoodId(foodId)

    override fun observeRecipeDB(foodId: Int): Flow<List<RecipeItem>> =
        recipeDao.observeRecipeDB(foodId)

    override fun getRecipe(recipeId: Int): RecipeItem? {
        return recipeDao.getRecipeByRecipeId(recipeId)
    }

    override fun saveRecipeItem(recipeItem: RecipeItem): Flow<Exception?> =
        flow {
            try {
                recipeDao.upsertRecipeItem(recipeItem)
                emit(null)
            } catch (e: Exception) {
                emit(e)
            }
        }
}