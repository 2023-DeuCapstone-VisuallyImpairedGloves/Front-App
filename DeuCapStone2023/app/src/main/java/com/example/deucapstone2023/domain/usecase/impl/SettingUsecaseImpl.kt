package com.example.deucapstone2023.domain.usecase.impl

import com.example.deucapstone2023.domain.repository.SettingRepository
import com.example.deucapstone2023.domain.usecase.SettingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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