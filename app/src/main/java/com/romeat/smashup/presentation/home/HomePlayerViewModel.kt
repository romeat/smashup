package com.romeat.smashup.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.dto.MashupMediaItem
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.musicservice.PlaybackRepeatMode
import com.romeat.smashup.musicservice.SmashupPlaybackState
import com.romeat.smashup.musicservice.isPlaying
import com.romeat.smashup.util.ImageUrlHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePlayerViewModel @Inject constructor(
    private val musicService: MusicServiceConnection,
    @ApplicationContext val context: Context,
    val loggedUserRepository: LoggedUserRepository // probably not the best place
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()

    var currentTimeMs by mutableStateOf(0L)

    private var collectingJob: Job? = null

    private val imageLoader = ImageLoaderJob()

    init {
        loggedUserRepository.updateUserStat()

        viewModelScope.launch {
            combine(
                musicService.playbackState,
                musicService.nowPlayingMashup,
                musicService.currentSongDuration,
            ) { playbackState: SmashupPlaybackState,
                nowPlaying: MashupMediaItem?,
                songDuration: Long ->
                PlayerState(
                    isPlaying = playbackState.rawState.isPlaying,
                    imageId = nowPlaying?.id ?: 0,
                    isShuffle = playbackState.isShuffle,
                    repeatMode = playbackState.repeatMode,
                    trackDurationMs = songDuration,
                    trackName = nowPlaying?.name ?: "",
                    trackAuthor = nowPlaying?.owner ?: "",
                    isPlaybackNull = nowPlaying == null,
                    coverSmall = _state.value.coverSmall
                )
            }.collect { value ->
                _state.update { value }

                if (value.isPlaybackNull) {
                    stopTimestampCollectingJob()
                } else if (collectingJob == null) {
                    startTimestampCollectingJob()
                }

                if (value.isPlaybackNull) {
                    imageLoader.cancel()
                } else {
                    imageLoader.load(value.imageId)
                }
            }
        }
    }

    private fun startTimestampCollectingJob() {
        collectingJob = viewModelScope.launch {
            while (true) {
                if (state.value.isPlaying) {
                    currentTimeMs =  musicService.getTime()
                } else if (state.value.isPlaybackNull) {
                    currentTimeMs = 0L
                }
                delay(500)
            }
        }
    }

    private fun stopTimestampCollectingJob() {
        collectingJob?.cancel()
        collectingJob = null
        currentTimeMs = 0L
    }

    fun onPlayPauseClick() {
        if (musicService.playbackState.value.rawState.isPlaying) {
            musicService.controls.pause()
        } else {
            musicService.controls.play()
        }
    }

    fun onNextClick() {
        musicService.controls.skipToNext()
    }

    fun onPreviousClick() {
        musicService.controls.skipToPrevious()
    }

    fun onShuffleClick() {
        musicService.shuffle()
    }

    fun onRepeatClick() {
        musicService.nextRepeatMode()
    }

    inner class ImageLoaderJob {

        private var job: Job? = null
        private var jobId: Int? = null

        fun load(id: Int) {
            // if already loading image with same id - do nothing
            if (jobId == id) {
                return
            }

            job?.cancel()
            jobId = id

            job = viewModelScope.launch {
                Glide.with(context)
                    .asBitmap()
                    .load(ImageUrlHelper.mashupImageIdToUrl100px(state.value.imageId.toString()))
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            _state.update { it.copy(coverSmall = resource.asImageBitmap()) }
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }

        fun cancel() {
            job?.cancel()
            _state.update {
                it.copy(coverSmall = null) }
        }
    }
}



data class PlayerState(
    val isPlaying: Boolean = false,
    val isShuffle: Boolean = false,
    val repeatMode: PlaybackRepeatMode = PlaybackRepeatMode.None,
    val imageId: Int = 0,
    val trackDurationMs: Long = 148000,
    val trackName: String = "Somebody once AAAAAAAAAA",
    val trackAuthor: String = "dobobob",
    val isPlaybackNull: Boolean = true,
    val coverSmall: ImageBitmap? = null
)
