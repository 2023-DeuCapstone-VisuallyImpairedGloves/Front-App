package com.example.deucapstone2023.domain.model.di

import com.example.deucapstone2023.domain.service.NavigationService
import com.example.deucapstone2023.domain.service.NavigationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationServiceModule {
    @Binds
    abstract fun bindsNavigationService(navigationServiceImpl: NavigationServiceImpl) : NavigationService
}