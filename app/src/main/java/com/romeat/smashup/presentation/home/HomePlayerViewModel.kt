package com.romeat.smashup.presentation.home

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.MashupUiData
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.musicservice.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePlayerViewModel @Inject constructor(
    private val musicService: MusicServiceConnection
) : ViewModel() {

    var state by mutableStateOf(PlayerState())
    var currentTimeMs by mutableStateOf(0L)

    private var collectingJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                musicService.playbackState,
                musicService.nowPlayingMashup,
                musicService.currentSongDuration,
            ) { playbackState: PlaybackStateCompat,
                nowPlaying: MashupUiData?,
                songDuration: Long ->
                PlayerState(
                    isPlaying = playbackState.isPlaying,
                    imageId = nowPlaying?.id ?: 0,
                    trackDurationMs = songDuration,
                    trackName = nowPlaying?.name ?: "",
                    trackAuthor = nowPlaying?.owner ?: "",
                    isPlaybackNull = nowPlaying == null
                )
            }.collect { value ->
                state = value
                if (value.isPlaybackNull) {
                    stopTimestampCollectingJob()
                } else if (collectingJob == null) {
                    startTimestampCollectingJob()
                }
            }
        }
    }

    private fun startTimestampCollectingJob() {
        collectingJob = viewModelScope.launch {
            while (true) {
                if (state.isPlaying) {
                    currentTimeMs =  musicService.getTime()
                } else if (state.isPlaybackNull) {
                    currentTimeMs = 0L
                }
                delay(500)
            }
        }
    }

    private fun stopTimestampCollectingJob() {
        collectingJob?.cancel()
        collectingJob = null
    }

    fun onPlayPauseClick() {
        if (musicService.playbackState.value.isPlaying) {
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
}

data class PlayerState(
    val isPlaying: Boolean = false,
    val imageId: Int = 0,
    val trackDurationMs: Long = 148000,
    val trackName: String = "Somebody once AAAAAAAAAA",
    val trackAuthor: String = "dobobob",
    val isPlaybackNull: Boolean = true
)
