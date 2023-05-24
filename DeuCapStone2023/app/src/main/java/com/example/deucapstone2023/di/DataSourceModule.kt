package com.example.deucapstone2023.di

import com.example.deucapstone2023.data.datasource.local.LocalLogDataSourceImpl
import com.example.deucapstone2023.data.datasource.local.LocalLogDatasource
import com.example.deucapstone2023.data.datasource.local.LocalSettingDatasource
import com.example.deucapstone2023.data.datasource.local.LocalSettingDatasourceImpl
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
    abstract fun bindsLocalSettingDataSource(localSettingDatasourceImpl: LocalSettingDatasourceImpl): LocalSettingDatasource

    @Singleton
    @Binds
    abstract fun bindsLocalLogDataSource(localLogDataSourceImpl: LocalLogDataSourceImpl): LocalLogDatasource
}