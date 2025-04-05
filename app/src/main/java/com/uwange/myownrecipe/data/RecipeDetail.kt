package com.uwange.myownrecipe.data

import com.google.gson.annotations.SerializedName

//TODO:: Data 형식 정해지면 처리
data class RecipeDetail(
    @SerializedName("recipeId")
    val recipeId: Int
)