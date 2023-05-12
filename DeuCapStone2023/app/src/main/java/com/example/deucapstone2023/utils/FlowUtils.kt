package com.example.deucapstone2023.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException

fun <T> Flow<T>.catchFetching(
    onFailedHttpException: suspend () -> Unit,
    onFailedElseException: suspend () -> Unit
): Flow<T> = this.catch { e ->
    when (e) {
        is HttpException -> {
            onFailedHttpException()
        }

        else -> {
            onFailedElseException()
        }
    }
}