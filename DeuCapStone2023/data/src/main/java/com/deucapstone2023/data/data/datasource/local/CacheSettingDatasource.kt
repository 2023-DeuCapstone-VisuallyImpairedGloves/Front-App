package com.deucapstone2023.data.data.datasource.local

import com.deucapstone2023.data.SettingPreferences
import kotlinx.coroutines.flow.Flow

interface CacheSettingDatasource {
    fun getConnectionStatus() : Flow<SettingPreferences>
    suspend fun setConnectionStatus(status: Boolean)
}