package com.uwange.myownrecipe.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    // Food 가 삭제 되면, Food 에 속한 Recipe 도 삭제
    foreignKeys = [
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["foodId"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["foodId"])] // foodId 컬럼에 대한 인덱스 생성
)
data class RecipeItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("recipeId")
    val recipeId: Int,
    @SerializedName("foodId")
    val foodId: Int,
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