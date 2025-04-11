package com.uwange.myownrecipe.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("foodId")
    val foodId: Int,
    @SerializedName("foodName")
    val foodName: String,
    @SerializedName("score")
    val score: String,
    @SerializedName("recipeReview")
    val recipeReview: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("imageDescription")
    val imageDescription: String
)