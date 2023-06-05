package com.deucapstone2023.app.ui.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException

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