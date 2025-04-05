package com.uwange.myownrecipe.di

import android.content.Context
import com.uwange.myownrecipe.data.db.FoodDB
import com.uwange.myownrecipe.data.db.RecipeDB
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
    fun provideFoodDB(@ApplicationContext context: Context): FoodDB =
        FoodDB.getDatabase(context)

    @Provides
    fun provideFoodDao(foodDB: FoodDB) =
        foodDB.foodDao()

    @Provides
    @Singleton
    fun provideRecipeDB(@ApplicationContext context: Context): RecipeDB =
        RecipeDB.getDatabase(context)

    @Provides
    fun provideRecipeDao(recipeDB: RecipeDB) =
        recipeDB.recipeDatabaseDao()

}