package com.example.deucapstone2023.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.deucapstone2023.SettingPreferences
import com.example.deucapstone2023.data.datasource.local.datastore.SettingPreferencesSerializer
import com.example.deucapstone2023.data.datasource.local.datastore.settingPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun provideSettingDataStore(@ApplicationContext context: Context): DataStore<SettingPreferences> {
        return context.settingPrefs
    }
}