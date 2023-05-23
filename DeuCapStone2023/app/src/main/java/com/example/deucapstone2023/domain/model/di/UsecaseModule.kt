package com.example.deucapstone2023.domain.model.di

import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.domain.usecase.SettingUsecase
import com.example.deucapstone2023.domain.usecase.impl.POIUsecaseImpl
import com.example.deucapstone2023.domain.usecase.impl.RouteUsecaseImpl
import com.example.deucapstone2023.domain.usecase.impl.SettingUsecaseImpl
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
}