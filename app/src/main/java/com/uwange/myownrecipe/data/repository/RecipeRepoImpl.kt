package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.data.dao.RecipeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepoImpl @Inject constructor(
    private val recipeDao: RecipeDao
): RecipeRepo {
    override fun getRecipeList(foodId: Int): ResponseForm<List<RecipeItem>> {
        return try {
            val recipeList = recipeDao.getRecipesByFoodId(foodId)
            ResponseForm.Success(recipeList)
        } catch (e: Exception) {
            ResponseForm.Error(e)
        }
    }

    override fun observeRecipeDB(foodId: Int): Flow<ResponseForm<List<RecipeItem>>> {
        return recipeDao.observeRecipeDB(foodId)
            .map { ResponseForm.Success(it) }
            .catch { e ->
                ResponseForm.Error(Exception("Failed to observe recipe database", e))
            }
    }

    override fun getRecipe(recipeId: Int): ResponseForm<RecipeItem> {
        return try {
            val recipeItem = recipeDao.getRecipeByRecipeId(recipeId)!!
            ResponseForm.Success(recipeItem)
        } catch (e: Exception) {
            ResponseForm.Error(e)
        }
    }

    override fun saveRecipeItem(recipeItem: RecipeItem): ResponseForm<Long> {
        return try {
            val rowId = recipeDao.insertRecipeItem(recipeItem)
            ResponseForm.Success(rowId)
        } catch (e: Exception) {
            ResponseForm.Error(e)
        }
    }
}