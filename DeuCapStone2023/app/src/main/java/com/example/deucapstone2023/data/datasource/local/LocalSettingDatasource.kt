package com.example.deucapstone2023.data.datasource.local

import com.example.deucapstone2023.SettingPreferences
import kotlinx.coroutines.flow.Flow

interface LocalSettingDatasource {
    fun getConnectionStatus() : Flow<SettingPreferences>
    suspend fun setConnectionStatus(status: Boolean)
}