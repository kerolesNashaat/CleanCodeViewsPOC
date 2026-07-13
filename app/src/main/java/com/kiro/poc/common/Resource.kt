package com.kiro.poc.common

import com.kiro.poc.data.error.DataError

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: DataError) : Resource<Nothing>()
}
