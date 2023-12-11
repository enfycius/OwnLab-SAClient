package com.ownlab.ownlab_client.utils

import com.google.gson.Gson
import com.ownlab.ownlab_client.models.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class Constants {
    fun<T> apiFlow(call: suspend() -> Response<T>): Flow<ApiResponse<T>> = flow {
        withTimeoutOrNull(10000L) {
            val response = call()

            try {
                if (response.isSuccessful) {
                    response.body()?.let { d ->
                        emit(ApiResponse.Success(d))
                    }
                } else {
                    response.errorBody()?.let { e ->
                        e.close()

                        val getError: ErrorResponse = Gson().fromJson(e.charStream(), ErrorResponse::class.java)
                        emit(ApiResponse.Failure(getError.message, getError.code))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Failure(e.message ?: e.toString(), 400))   // 400 Bad Request
            }
        } ?: emit(ApiResponse.Failure("Timed Out...", 408)) // 408 Request Timeout
    }.flowOn(Dispatchers.IO)
}