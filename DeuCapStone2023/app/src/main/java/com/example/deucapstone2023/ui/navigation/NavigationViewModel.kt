package com.example.deucapstone2023.ui.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor():ViewModel(){
    private val mutableBottomNavigationBarState = MutableStateFlow(true)
    val bottomBarState get() = mutableBottomNavigationBarState.asStateFlow()

    fun setBottomNavigationState(bool:Boolean) {
        mutableBottomNavigationBarState.update { state -> bool }
    }
}