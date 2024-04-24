package com.rm.myrecipes.data

import com.rm.myrecipes.data.common.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

interface BaseRepository {

    suspend fun <T> apiCall(
        dispatcher: CoroutineDispatcher,
        call: suspend () -> Response<T>
    ): Result<T> = withContext(dispatcher) {
        try {
            val response = call()
            if (!response.isSuccessful) {
                error(response.code(), response.message())
            } else {
                if (response.body() == null) {
                    error(response.code(), response.message())
                } else {
                    Result.OK<T>(response.body()!!)
                }
            }
        } catch (e: Exception) {
            Result.NetworkError(e)
        }
    }

    private fun error(code: Int, message: String): Result.NetworkError = when (code) {
        in 400..409 -> Result.NetworkError(mapNetworkBaseThrowable(code, "Client Error: $message"))
        in 500..599 -> Result.NetworkError(mapNetworkBaseThrowable(code, "Server Error: $message"))
        else -> Result.NetworkError(IOException("Unknown Error"))
    }

    private fun mapNetworkBaseThrowable(code: Int, message: String): Exception = RuntimeException("$code $message")
}