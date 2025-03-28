package com.uwange.myownrecipe.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class FoodArgumentData(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
): Parcelable