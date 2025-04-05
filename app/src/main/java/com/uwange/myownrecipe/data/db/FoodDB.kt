package com.uwange.myownrecipe.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.dao.FoodDao

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
abstract class FoodDB: RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: FoodDB? = null

        fun getDatabase(context: Context): FoodDB =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FoodDB::class.java,
                    "food_database"
                ).build().apply { INSTANCE = this }
            }
    }
}