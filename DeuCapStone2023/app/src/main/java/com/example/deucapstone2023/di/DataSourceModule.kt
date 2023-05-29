package com.example.deucapstone2023.di

import com.example.deucapstone2023.data.datasource.local.CacheLogDataSourceImpl
import com.example.deucapstone2023.data.datasource.local.CacheLogDatasource
import com.example.deucapstone2023.data.datasource.local.CacheSettingDatasource
import com.example.deucapstone2023.data.datasource.local.CacheSettingDatasourceImpl
import com.example.deucapstone2023.data.datasource.remote.FetchPOIDatasource
import com.example.deucapstone2023.data.datasource.remote.FetchPOIDatasourceImpl
import com.example.deucapstone2023.data.datasource.remote.FetchRouteDatasource
import com.example.deucapstone2023.data.datasource.remote.FetchRouteDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsLocalSettingDataSource(cacheSettingDatasourceImpl: CacheSettingDatasourceImpl): CacheSettingDatasource

    @Singleton
    @Binds
    abstract fun bindsLocalLogDataSource(cacheLogDataSourceImpl: CacheLogDataSourceImpl): CacheLogDatasource

    @Singleton
    @Binds
    abstract fun bindsFetchPOIDataSource(fetchPOIDatasourceImpl: FetchPOIDatasourceImpl) : FetchPOIDatasource

    @Singleton
    @Binds
    abstract fun bindsFetchRouteDataSource(fetchRouteDatasourceImpl: FetchRouteDatasourceImpl) : FetchRouteDatasource
}