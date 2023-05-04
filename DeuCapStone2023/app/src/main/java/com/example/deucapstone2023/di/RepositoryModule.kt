package com.example.deucapstone2023.di

import com.example.deucapstone2023.data.repositoryimpl.POIRepositoryImpl
import com.example.deucapstone2023.data.repositoryimpl.RouteRepositoryImpl
import com.example.deucapstone2023.domain.repository.POIRepository
import com.example.deucapstone2023.domain.repository.RouteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsPOIRepository(poiRepositoryImpl: POIRepositoryImpl) : POIRepository

    @Singleton
    @Binds
    abstract fun bindsRouteRepository(routeRepositoryImpl: RouteRepositoryImpl) : RouteRepository
}