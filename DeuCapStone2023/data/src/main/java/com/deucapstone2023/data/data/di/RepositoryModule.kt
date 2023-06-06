package com.deucapstone2023.data.data.di

import com.deucapstone2023.data.data.datasource.local.CacheLogDatasource
import com.deucapstone2023.data.data.datasource.local.CacheSettingDatasource
import com.deucapstone2023.data.data.datasource.remote.FetchPOIDatasource
import com.deucapstone2023.data.data.datasource.remote.FetchRouteDatasource
import com.deucapstone2023.data.data.repositoryimpl.LogRepositoryImpl
import com.deucapstone2023.data.data.repositoryimpl.POIRepositoryImpl
import com.deucapstone2023.data.data.repositoryimpl.RouteRepositoryImpl
import com.deucapstone2023.data.data.repositoryimpl.SettingRepositoryImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.deucapstone2023.domain.domain.repository.POIRepository
import com.deucapstone2023.domain.domain.repository.RouteRepository
import  com.deucapstone2023.domain.domain.repository.SettingRepository
import  com.deucapstone2023.domain.domain.repository.LogRepository
import dagger.Provides

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Singleton
    @Provides
    fun bindsPOIRepository(poiDatasource: FetchPOIDatasource): POIRepository {
        return POIRepositoryImpl(poiDatasource)
    }

    @Singleton
    @Provides
    fun bindsRouteRepository(routeDatasource: FetchRouteDatasource): RouteRepository {
        return RouteRepositoryImpl(routeDatasource)
    }

    @Singleton
    @Provides
    fun bindsSettingRepository(settingDatasource: CacheSettingDatasource): SettingRepository {
        return SettingRepositoryImpl(settingDatasource)
    }

    @Singleton
    @Provides
    fun bindsLogRepository(logDatasource: CacheLogDatasource): LogRepository {
        return LogRepositoryImpl(logDatasource)
    }
}