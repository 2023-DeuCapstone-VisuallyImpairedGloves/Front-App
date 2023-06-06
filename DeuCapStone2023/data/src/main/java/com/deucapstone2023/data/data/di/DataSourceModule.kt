package com.deucapstone2023.data.data.di

import com.deucapstone2023.data.data.datasource.remote.FetchPOIDatasource
import com.deucapstone2023.data.data.datasource.remote.FetchPOIDatasourceImpl
import com.deucapstone2023.data.data.datasource.remote.FetchRouteDatasource
import com.deucapstone2023.data.data.datasource.remote.FetchRouteDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.deucapstone2023.data.data.datasource.local.CacheSettingDatasourceImpl
import com.deucapstone2023.data.data.datasource.local.CacheLogDataSourceImpl
import com.deucapstone2023.data.data.datasource.local.CacheLogDatasource
import com.deucapstone2023.data.data.datasource.local.CacheSettingDatasource

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

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