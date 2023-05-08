package com.example.deucapstone2023.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException
import java.lang.Exception

fun <T> Flow<T>.catchFetching(
    onFailedHttpException: suspend (e: HttpException) -> Unit,
    onFailedElseException: suspend (e: Throwable) -> Unit
): Flow<T> = this.catch { e ->
    when (e) {
        is HttpException -> {
            onFailedHttpException(e)
        }

        else -> {
            onFailedElseException(e)
        }
    }
}