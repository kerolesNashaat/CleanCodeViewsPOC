package com.kiro.poc.data.error

sealed class DataError {
    data object NoInternet : DataError()
    data class Network(val code: Int? = null, val message: String? = null) : DataError()
    data class Serialization(val message: String? = null) : DataError()
    data class Unknown(val message: String? = null) : DataError()
}
