package com.example.deucapstone2023.ui.di

import android.content.Context
import com.example.deucapstone2023.R
import com.skt.tmap.TMapView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object TMapModule {

    @Provides
    fun providesTMapView(@ActivityContext context: Context): TMapView {
        return TMapView(context).apply {
            setSKTMapApiKey(context.getString(R.string.T_Map_key))
        }
    }
}