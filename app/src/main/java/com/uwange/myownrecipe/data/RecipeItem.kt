package com.uwange.myownrecipe.data

import com.google.gson.annotations.SerializedName

data class RecipeItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("imageDescription")
    val imageDescription: String,
    @SerializedName("bookmark")
    var bookmark: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("score")
    val score: Float,
    @SerializedName("recipeReview")
    val recipeReview: String
)