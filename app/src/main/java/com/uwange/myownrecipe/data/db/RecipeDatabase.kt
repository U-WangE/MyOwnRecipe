package com.uwange.myownrecipe.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.data.RecipeItem

@Database(entities = [RecipeItem::class, FoodItem::class], version = 1, exportSchema = false)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDatabaseDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build().apply { INSTANCE = this }
            }
    }
}