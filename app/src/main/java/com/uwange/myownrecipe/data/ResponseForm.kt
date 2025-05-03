package com.uwange.myownrecipe.data

sealed class ResponseForm<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): ResponseForm<T>(data = data)
    class Error(val exception: Exception) : ResponseForm<Nothing>()
}