package com.romeat.smashup.presentation.home.settings.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.user.profile.ConfirmPasswordUpdateUseCase
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmPasswordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val confirmPasswordUseCase: ConfirmPasswordUpdateUseCase
) : ViewModel() {

    var state by mutableStateOf(ConfirmPasswordState())

    private val token: String =
        checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            confirmPasswordUseCase
                .invoke(token)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            state = state.copy(
                                isLoading = false,
                                showSuccess = true
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                isError = true,
                                errorResId = when (result.code) {
                                    404 -> R.string.error_404_request_unknown
                                    500 -> R.string.error_500_server_problems
                                    else -> R.string.error_general
                                }
                            )
                        }
                    }
                }
        }
    }
}

data class ConfirmPasswordState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorResId: Int = R.string.string_empty,
    val showSuccess: Boolean = false,
)