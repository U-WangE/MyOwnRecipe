package com.uwange.myownrecipe.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.uwange.myownrecipe.data.RecipeItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("Select * From RecipeItem Where foodId = :foodId")
    fun getRecipesByFoodId(foodId: Int): List<RecipeItem>

    @Query("Select * From RecipeItem Where foodId = :foodId")
    fun observeFoodDB(foodId: Int): Flow<List<RecipeItem>>

    @Query("Select * From RecipeItem Where recipeId = :recipeId")
    fun getRecipeByRecipeId(recipeId: Int): RecipeItem
}