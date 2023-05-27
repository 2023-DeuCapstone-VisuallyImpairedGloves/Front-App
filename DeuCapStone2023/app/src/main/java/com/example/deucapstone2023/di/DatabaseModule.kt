package com.example.deucapstone2023.di

import android.content.Context
import androidx.room.Room
import com.example.deucapstone2023.data.datasource.local.database.Database
import com.example.deucapstone2023.data.datasource.local.database.dao.LocalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "myDatabase")
            .createFromAsset("capstone_db.db")
            .build()
    }

    @Provides
    @Singleton
    fun providesDatabaseDao(database: Database): LocalService {
        return database.localDao()
    }
}