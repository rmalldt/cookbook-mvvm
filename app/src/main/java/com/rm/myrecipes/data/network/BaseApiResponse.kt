package com.rm.myrecipes.data.network

import com.rm.myrecipes.data.common.Result
import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            return if (!response.isSuccessful) {
                Result.NetworkError(genericException(response.code(), response.message()))
            } else {
                return if (response.body() == null) {
                    Result.NetworkError(genericException(response.code(), "Response body is null"))
                } else {
                    Result.OK(response.body()!!)
                }
            }
        } catch (e: Exception) {
            return Result.NetworkError(e)
        }
    }

    private fun genericException(code: Int, message: String) = RuntimeException("$code $message")
}