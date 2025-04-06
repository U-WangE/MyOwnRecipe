package com.uwange.myownrecipe.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeArgumentData(
    @SerializedName("recipeId")
    var recipeId: Int,
    @SerializedName("foodName")
    var foodName: String
): Parcelable {
    constructor() : this(-1, "")
}