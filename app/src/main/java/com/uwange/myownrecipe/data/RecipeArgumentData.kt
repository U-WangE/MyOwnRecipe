package com.uwange.myownrecipe.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeArgumentData(
    @SerializedName("recipeId")
    var recipeId: Int,
    @SerializedName("foodId")
    var foodId: Int
): Parcelable {
    constructor() : this(-1, -1)
}