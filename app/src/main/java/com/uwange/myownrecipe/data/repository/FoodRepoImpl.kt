package com.uwange.myownrecipe.data.repository

import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.data.dao.FoodDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FoodRepoImpl @Inject constructor(
    private val foodDao: FoodDao,
): FoodRepo {
    override suspend fun getFood(foodId: Int): ResponseForm<FoodItem> {
        return try {
            val foodItem = foodDao.getFood(foodId)!!
            ResponseForm.Success(foodItem)
        } catch(e: Exception) {
            ResponseForm.Error(e)
        }
    }

    override fun observeFoodDB(): Flow<ResponseForm<List<FoodItem>>> {
        return foodDao.observeFoodDB()
            .map { ResponseForm.Success(it) }
            .catch { e ->
                ResponseForm.Error(Exception("Failed to observe food database", e))
            }
    }

    override fun isFoodNameAlreadyExist(foodName: String): Boolean {
        return try {
            foodDao.isFoodNameAlreadyExist(foodName)
        } catch (e: Exception) {
            false
        }
    }

    override fun saveFoodItem(foodName: String): ResponseForm<Long> {
        return try {
            val rowId = foodDao.insertFoodItem(foodItem = FoodItem(foodName = foodName))
            ResponseForm.Success(rowId)
        } catch(e: Exception) {
            ResponseForm.Error(e)
        }
    }
}