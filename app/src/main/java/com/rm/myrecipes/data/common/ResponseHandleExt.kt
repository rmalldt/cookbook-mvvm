package com.rm.myrecipes.data.common

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.io.IOException
import kotlin.Result

suspend fun <T, R> handleResponse(
    request: suspend () -> Response<T>,
    responseMapper: (T) -> R
): Result<R> {
    return try {
        request().let { response ->
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Result.success(responseMapper(result))
            } else {
                val errorResponse = response.errorBody()?.let { getErrorResponse(it.string()) }
                Result.failure(createServerError(errorResponse))
            }
        }
    } catch (e: Exception) {
        Result.failure(NetworkError("e: $e, cause: ${e.cause}"))
    }
}

private fun createServerError(errorResponse: ErrorResponse? ): Exception {
    val status = errorResponse?.status
    val code = errorResponse?.code
    val message = errorResponse?.message
    return NetworkError("status:$status, code:$code, message:$message")
}

private fun getErrorResponse(errorBody: String): ErrorResponse {
    return Gson().fromJson(errorBody, ErrorResponse::class.java)
}

data class ErrorResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?
)

class NetworkError(override val message: String) : IOException()