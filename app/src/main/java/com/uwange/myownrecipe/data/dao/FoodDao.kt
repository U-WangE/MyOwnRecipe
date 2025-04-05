package com.uwange.myownrecipe.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.uwange.myownrecipe.data.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("Select * From FoodItem")
    fun getFoodList(): List<FoodItem>

    @Query("Select * From FoodItem")
    fun observeFoodDB(): Flow<List<FoodItem>>
}