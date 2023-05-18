package com.example.deucapstone2023.domain.usecase

import com.example.deucapstone2023.SettingPreferences
import kotlinx.coroutines.flow.Flow

interface SettingUsecase {
    fun getConnectionStatus() : Flow<Boolean>
    suspend fun setConnectionStatus(status: Boolean)
}