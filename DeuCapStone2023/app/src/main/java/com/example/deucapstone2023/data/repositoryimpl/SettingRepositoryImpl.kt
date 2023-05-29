package com.example.deucapstone2023.data.repositoryimpl

import com.example.deucapstone2023.data.datasource.local.CacheSettingDatasource
import com.example.deucapstone2023.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDatasource: CacheSettingDatasource
) : SettingRepository{
    override fun getConnectionStatus(): Flow<Boolean> = flow {
        settingDatasource.getConnectionStatus().collect { prefs ->
            emit(prefs.status)
        }
    }


    override suspend fun setConnectionStatus(status: Boolean) {
        settingDatasource.setConnectionStatus(status)
    }
}