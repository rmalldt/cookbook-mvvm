package com.rm.myrecipes.data.common

import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

sealed class Result<out T> {
    class OK<T>(val data: T): Result<T>()
    class NetworkError(val exception: Throwable): Result<Nothing>()
}

object SafeApiCall {
    suspend fun <T> call(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            return if (!response.isSuccessful) {
                error(response.code(), response.message())
            } else {
                return if (response.body() == null) {
                    error(response.code(), response.message())
                } else {
                    Result.OK<T>(response.body()!!)
                }
            }
        } catch (e: Exception) {
            return Result.NetworkError(e)
        }
    }

    private fun error(code: Int, message: String): Result.NetworkError = when (code) {
            in 400..409 -> Result.NetworkError(mapNetworkBaseThrowable(code, "Client Error: $message"))
            in 500..599 -> Result.NetworkError(mapNetworkBaseThrowable(code, "Server Error: $message"))
            else -> Result.NetworkError(IOException("Unknown Error"))
        }

    private fun mapNetworkBaseThrowable(code: Int, message: String): Exception = RuntimeException("$code $message")
}

