package com.example.deucapstone2023.ui.screen.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginUiState(
    val id: String,
    val pw: String
) {
    companion object {
        fun getInitValues() = LoginUiState(
            id = "",
            pw = ""
        )
    }
}
@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
}