package com.example.deucapstone2023.di

import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.impl.POIUsecaseImpl
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
}