package com.romeat.smashup.util

import com.romeat.smashup.network.util.ApiWrap

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Loading<T>: Resource<T>(null)
    class Error<T>(val exception: Exception,  val code: Int, message: String?, data: ApiWrap<*>? = null): Resource<T>(message = message)
}
