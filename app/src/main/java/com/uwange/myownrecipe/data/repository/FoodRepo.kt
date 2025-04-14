package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.ResponseForm
import kotlinx.coroutines.flow.Flow

interface FoodRepo {
    suspend fun getFood(foodId: Int): ResponseForm<FoodItem>
    fun observeFoodDB(): Flow<ResponseForm<List<FoodItem>>>
    fun isFoodNameAlreadyExist(foodName: String): Boolean
    fun saveFoodItem(foodName: String): ResponseForm<Long>
}