package com.example.deucapstone2023.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.deucapstone2023.SettingPreferences

private const val DATA_STORE_FILE_NAME = "setting_prefs.pb"

val Context.settingPrefs: DataStore<SettingPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = SettingPreferencesSerializer
)