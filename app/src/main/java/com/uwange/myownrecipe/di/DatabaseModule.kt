package com.uwange.myownrecipe.di

import android.content.Context
import com.uwange.myownrecipe.data.db.FoodDao
import com.uwange.myownrecipe.data.db.FoodDatabase
import com.uwange.myownrecipe.data.db.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFoodDatabase(@ApplicationContext context: Context): FoodDatabase =
        FoodDatabase.getDatabase(context)

    @Provides
    fun provideFoodDao(foodDatabase: FoodDatabase) =
        foodDatabase.foodDao()

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase =
        RecipeDatabase.getDatabase(context)

    @Provides
    fun provideRecipeDao(recipeDatabase: RecipeDatabase) =
        recipeDatabase.recipeDatabaseDao()

}