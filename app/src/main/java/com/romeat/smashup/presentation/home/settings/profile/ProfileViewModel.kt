package com.romeat.smashup.presentation.home.settings.profile

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.user.profile.UpdateAvatarUseCase
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loggedUser: LoggedUserRepository,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ProfileEvent>(Channel.BUFFERED)
    val events = _eventChannel.receiveAsFlow()

    init {
        val user = loggedUser.userInfoFlow.value
        user?.let { presentUser ->
            _state.update {
                it.copy(
                    nickname = presentUser.username,
                    email = "блять, емаил неоткуда взять",
                    passwordDots = "***********",
                    imageUrl = presentUser.imageUrl
                )
            }
        }
    }

    fun updateAvatar(bitmap: Bitmap) {
        val str = encodeImage(bitmap)
//        Log.d("TAG", "updateAvatar: $str")
        str?.let {
            viewModelScope.launch {
                updateAvatarUseCase
                    .invoke(str)
                    .collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                _eventChannel.send(ProfileEvent.ShowToast(R.string.toast_avatar_fail))
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                _eventChannel.send(ProfileEvent.ShowToast(R.string.toast_avatar_updated))
                            }
                        }
                    }
            }
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val maxHeight = 800
        val maxWidth = 800
        val scale: Float = Math.min(
            maxHeight.toFloat() / bm.width,
            maxWidth.toFloat() / bm.height
        )

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        val scaledBitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)

        val baos = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}


sealed class ProfileEvent {
    data class ShowToast(val messageResId: Int) : ProfileEvent()
}

data class ProfileState(
    val nickname: String = "",
    val email: String = "",
    val passwordDots: String = "",
    val imageUrl: String = ""
)