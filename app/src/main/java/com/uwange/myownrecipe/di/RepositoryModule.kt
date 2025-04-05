package com.uwange.myownrecipe.di

import com.uwange.myownrecipe.data.repository.FoodRepo
import com.uwange.myownrecipe.data.repository.FoodRepoImpl
import com.uwange.myownrecipe.data.repository.RecipeRepo
import com.uwange.myownrecipe.data.repository.RecipeRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFoodRepo(): FoodRepo =
        FoodRepoImpl()

    @Provides
    @Singleton
    fun provideRecipeRepo(): RecipeRepo =
        RecipeRepoImpl()
}