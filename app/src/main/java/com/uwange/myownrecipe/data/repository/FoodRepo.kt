package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.FoodItem
import kotlinx.coroutines.flow.Flow

interface FoodRepo {
    fun getFoodList(): List<FoodItem>
    fun observeFoodDB(): Flow<List<FoodItem>>
}