package com.deucapstone2023.domain.domain.usecase.impl

import com.deucapstone2023.domain.domain.repository.SettingRepository
import com.deucapstone2023.domain.domain.usecase.SettingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingUsecaseImpl @Inject constructor(
    private val settingRepository: SettingRepository
) : SettingUsecase {
    override fun getConnectionStatus(): Flow<Boolean> =
        settingRepository.getConnectionStatus()


    override suspend fun setConnectionStatus(status: Boolean) {
        settingRepository.setConnectionStatus(status)
    }
}