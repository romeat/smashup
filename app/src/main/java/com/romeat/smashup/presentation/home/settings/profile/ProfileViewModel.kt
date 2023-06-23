package com.romeat.smashup.presentation.home.settings.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : ViewModel() {

    val state by mutableStateOf(ProfileState())

    init {

    }
}

data class ProfileState(
    val nickname: String = "",
    val email: String = "",
    val passwordDots: String = "",
    val imageUrl: String = ""
)