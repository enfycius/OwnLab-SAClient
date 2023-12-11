package com.ownlab.ownlab_client.utils

sealed class ApiResponse<out T> {
    data class Success<out T>(
        val data: T
    ): ApiResponse<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int,
    ): ApiResponse<Nothing>()
}