package com.uwange.myownrecipe.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.data.dao.RecipeDao

@Database(entities = [RecipeItem::class, FoodItem::class], version = 1, exportSchema = false)
abstract class RecipeDB: RoomDatabase() {
    abstract fun recipeDatabaseDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDB? = null

        fun getDatabase(context: Context): RecipeDB =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    RecipeDB::class.java,
                    "recipe_database"
                ).build().apply { INSTANCE = this }
            }
    }
}