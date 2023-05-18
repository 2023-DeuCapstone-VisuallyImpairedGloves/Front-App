package com.example.deucapstone2023.domain.repository

import com.example.deucapstone2023.SettingPreferences
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun getConnectionStatus() : Flow<Boolean>
    suspend fun setConnectionStatus(status: Boolean)
}