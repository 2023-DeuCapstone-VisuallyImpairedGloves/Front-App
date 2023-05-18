package com.example.deucapstone2023.data.datasource.local

import androidx.datastore.core.DataStore
import com.example.deucapstone2023.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class LocalSettingDatasourceImpl @Inject constructor(
    private val datastore: DataStore<SettingPreferences>
) : LocalSettingDatasource {
    override fun getConnectionStatus(): Flow<SettingPreferences> =
        datastore.data.catch { e ->
            if (e is IOException)
                emit(SettingPreferences.getDefaultInstance())
            else
                throw e
        }


    override suspend fun setConnectionStatus(status: Boolean) {
        datastore.updateData { ds -> ds.toBuilder().setStatus(status).build() }
    }
}