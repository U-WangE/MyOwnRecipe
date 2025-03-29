package com.uwange.myownrecipe.data

import com.google.gson.annotations.SerializedName

data class FoodItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("imageDescription")
    val imageDescription: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("score")
    val score: Float,
    @SerializedName("supportText")
    val supportText: String
)