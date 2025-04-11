package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.dao.FoodDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FoodRepoImpl @Inject constructor(
    private val foodDao: FoodDao
): FoodRepo {
    override fun getFood(foodId: Int): FoodItem? =
        foodDao.getFood(foodId)

    override fun getFoodList(): List<FoodItem> =
        foodDao.getFoodList()

    override fun observeFoodDB(): Flow<List<FoodItem>> =
        foodDao.observeFoodDB()
}