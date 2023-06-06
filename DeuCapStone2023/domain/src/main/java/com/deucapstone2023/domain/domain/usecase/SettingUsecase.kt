package com.deucapstone2023.domain.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingUsecase {
    fun getConnectionStatus() : Flow<Boolean>
    suspend fun setConnectionStatus(status: Boolean)
}