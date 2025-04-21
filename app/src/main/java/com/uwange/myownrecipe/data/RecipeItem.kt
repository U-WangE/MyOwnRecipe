package com.uwange.myownrecipe.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class RecipeItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("recipeId")
    val recipeId: Int? = null,
    @SerializedName("foodId")
    var foodId: Int,
    @SerializedName("recipeName")
    var recipeName: String,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("imageDescription")
    var imageDescription: String,
    @SerializedName("bookmark")
    var bookmark: Boolean,
    @SerializedName("score")
    var score: String,
    @SerializedName("ingredients")
    var ingredients: String,
    @SerializedName("recipeSteps")
    var recipeSteps: String,
    @SerializedName("recipeReview")
    var recipeReview: String
): Parcelable {
    constructor(foodId: Int): this(
        recipeId = null,
        foodId = foodId,
        recipeName = "",
        imageUrl = "",
        imageDescription = "",
        bookmark = false,
        score = "",
        ingredients = "",
        recipeSteps = "",
        recipeReview = ""
    )
}