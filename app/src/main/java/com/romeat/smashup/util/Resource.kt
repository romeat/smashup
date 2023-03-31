package com.romeat.smashup.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Loading<T>: Resource<T>(null)
    class Error<T>(exception: Exception, message: String, data: T? = null, ): Resource<T>(data, message)
}
