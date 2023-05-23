package com.example.deucapstone2023.ui.di

import com.example.deucapstone2023.ui.screen.search.state.NavigationManager
import com.example.deucapstone2023.ui.screen.search.state.NavigationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object NavigationManagerModule {

    @Provides
    fun providesNavigationManager(): NavigationManagerImpl {
        return NavigationManagerImpl()
    }

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingNavigationModule {
    @Binds
    abstract fun bindsNavigationManager(navigationManagerImpl: NavigationManagerImpl) : NavigationManager
}