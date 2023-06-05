package com.deucapstone2023.app.ui.screen.temp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HomeUiState(
    val title: String
) {
    companion object {
        fun getInitValues() = HomeUiState(
            title = ""
        )
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState.getInitValues())
    val homeUiState get() = _homeUiState.asStateFlow()

    fun setTitle(title: String) = _homeUiState.update { state ->
        state.copy(title = title)
    }
}