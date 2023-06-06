package com.deucapstone2023.data.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.deucapstone2023.domain.domain.repository.SettingRepository

class SettingRepositoryImpl @Inject constructor(
    private val settingDatasource: com.deucapstone2023.data.data.datasource.local.CacheSettingDatasource
) : SettingRepository {
    override fun getConnectionStatus(): Flow<Boolean> = flow {
        settingDatasource.getConnectionStatus().collect { prefs ->
            emit(prefs.status)
        }
    }


    override suspend fun setConnectionStatus(status: Boolean) {
        settingDatasource.setConnectionStatus(status)
    }
}