package com.deucapstone2023.domain.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun getConnectionStatus() : Flow<Boolean>
    suspend fun setConnectionStatus(status: Boolean)
}