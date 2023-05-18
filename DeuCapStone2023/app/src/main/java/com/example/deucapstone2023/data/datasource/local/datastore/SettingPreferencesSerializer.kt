package com.example.deucapstone2023.data.datasource.local.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.deucapstone2023.SettingPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingPreferencesSerializer : Serializer<SettingPreferences> {

    override val defaultValue: SettingPreferences = SettingPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingPreferences {
        return try {
            SettingPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SettingPreferences, output: OutputStream) = t.writeTo(output)
}