package com.deucapstone2023.data.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.deucapstone2023.data.SettingPreferences
import com.deucapstone2023.data.data.datasource.local.datastore.settingPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    fun provideSettingDataStore(@ApplicationContext context: Context): DataStore<SettingPreferences> {
        return context.settingPrefs
    }
}