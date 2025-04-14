package com.uwange.myownrecipe.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uwange.myownrecipe.data.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("Select * From FoodItem Where foodId = :foodId")
    fun getFood(foodId: Int): FoodItem?

    @Query("Select * From FoodItem")
    fun getFoodList(): List<FoodItem>

    @Query("Select * From FoodItem")
    fun observeFoodDB(): Flow<List<FoodItem>>

    @Query("Select Exists(Select 1 From FoodItem Where foodName = :foodName)")
    fun isFoodNameAlreadyExist(foodName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFoodItem(foodItem: FoodItem): Long
}