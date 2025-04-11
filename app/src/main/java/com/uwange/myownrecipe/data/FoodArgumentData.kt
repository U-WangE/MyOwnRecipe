package com.uwange.myownrecipe.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class FoodArgumentData(
    @SerializedName("foodId")
    var foodId: Int,
    @SerializedName("foodName")
    var foodName: String
): Parcelable {
    constructor() : this(-1, "")
}