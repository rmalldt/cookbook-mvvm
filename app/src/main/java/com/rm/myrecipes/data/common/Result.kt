package com.rm.myrecipes.data.common

sealed class Result<out T> {
    class OK<T>(val data: T): Result<T>()
    class NetworkError(val exception: Throwable): Result<Nothing>()
}
