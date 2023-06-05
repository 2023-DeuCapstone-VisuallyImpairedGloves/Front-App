package com.deucapstone2023.data.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.deucapstone2023.data.data.datasource.local.database.Database

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "myDatabase")
            .createFromAsset("capstone_db.db")
            .build()
    }

    @Provides
    @Singleton
    fun providesDatabaseDao(database: Database): com.deucapstone2023.data.data.datasource.local.database.dao.CacheLocal {
        return database.localDao()
    }
}