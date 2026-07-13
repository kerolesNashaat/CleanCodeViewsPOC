package com.kiro.poc.data.remote

import com.kiro.poc.common.Resource
import com.kiro.poc.data.error.DataError
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: CancellationException) {
        throw e
    } catch (e: SerializationException) {
        Resource.Error(DataError.Serialization(e.message))
    } catch (e: HttpException) {
        Resource.Error(DataError.Network(code = e.code(), message = e.message()))
    } catch (e: IOException) {
        Resource.Error(DataError.NoInternet)
    } catch (e: Exception) {
        Resource.Error(DataError.Unknown(e.message))
    }
}
