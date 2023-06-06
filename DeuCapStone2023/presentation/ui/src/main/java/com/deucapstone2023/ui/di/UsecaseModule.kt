package com.deucapstone2023.ui.di

import com.deucapstone2023.domain.domain.usecase.LogUsecase
import com.deucapstone2023.domain.domain.usecase.POIUsecase
import com.deucapstone2023.domain.domain.usecase.RouteUsecase
import com.deucapstone2023.domain.domain.usecase.SettingUsecase
import com.deucapstone2023.domain.domain.usecase.impl.LogUsecaseImpl
import com.deucapstone2023.domain.domain.usecase.impl.POIUsecaseImpl
import com.deucapstone2023.domain.domain.usecase.impl.RouteUsecaseImpl
import com.deucapstone2023.domain.domain.usecase.impl.SettingUsecaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsecaseModule {

    @Singleton
    @Binds
    abstract fun bindsPOIUsecase(poiUsecaseImpl: POIUsecaseImpl): POIUsecase

    @Singleton
    @Binds
    abstract fun bindsRouteUsecase(routeUsecaseImpl: RouteUsecaseImpl): RouteUsecase

    @Singleton
    @Binds
    abstract fun bindsSettingUsecase(settingUsecaseImpl: SettingUsecaseImpl): SettingUsecase

    @Singleton
    @Binds
    abstract fun bindsLogUsecase(logUsecaseImpl: LogUsecaseImpl): LogUsecase
}