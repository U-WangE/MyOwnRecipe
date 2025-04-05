package com.uwange.myownrecipe.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("foodId")
    val foodId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("imageDescription")
    val imageDescription: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("score")
    val score: Float,
    @SerializedName("recipeReview")
    val recipeReview: String
)