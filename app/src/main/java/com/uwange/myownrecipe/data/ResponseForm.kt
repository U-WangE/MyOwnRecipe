package com.uwange.myownrecipe.data

sealed class ResponseForm<out T>(
    val data: T? = null,
    val message: String? = null
) {
    data object Loading : ResponseForm<Nothing>()
    data object Success: ResponseForm<Nothing>()
    class Error<T>(message: String, data: T? = null) : ResponseForm<T>(data, message)
}