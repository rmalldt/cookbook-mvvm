package com.rm.myrecipes.data.common

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T> makeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> Response<T>
): Result<T> = withContext(dispatcher) {
    try {
        val response = call()
        val result = response.body()

        when {
            response.isSuccessful && result != null -> Result.OK<T>(result)
            else -> {
                val errorResponse = getErrorResponse(response.errorBody().toString())
                Result.NetworkError(createNetworkException(response.code(), errorResponse))
            }
        }
    } catch (e: Exception) {
        Result.NetworkError(e)
    }
}

private fun createNetworkException(code: Int, errorResponse: ErrorResponse): Exception {
    val status = errorResponse.status
    val cause = errorResponse.cause
    val message = errorResponse.message
    return NetworkException("code: $code, status: $status, cause: $cause, message: $message")
}

private fun getErrorResponse(errorBody: String): ErrorResponse {
    return Gson().fromJson(errorBody, ErrorResponse::class.java)
}

data class ErrorResponse(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("cause") val cause: String?,
    @SerializedName("message") val message: String?
)

class NetworkException(override val message: String) : RuntimeException()