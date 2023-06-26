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
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.data.likes.LikesState
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.musicservice.PlaybackRepeatMode
import com.romeat.smashup.musicservice.SmashupPlaybackState
import com.romeat.smashup.musicservice.isPlaying
import com.romeat.smashup.util.ImageUrlHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePlayerViewModel @Inject constructor(
    private val musicService: MusicServiceConnection,
    @ApplicationContext val context: Context,
    val likesRepository: LikesRepository,
    val loggedUserRepository: LoggedUserRepository // probably not the best place
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()

    var currentTimeMs by mutableStateOf(0L)

    private var collectingJob: Job? = null

    private val imageLoader = ImageLoaderJob()

    init {
        //loggedUserRepository.updateUserStat()

        viewModelScope.launch {
            combine(
                musicService.playbackState,
                musicService.nowPlayingMashup.debounce(100),
                musicService.currentSongDuration,
                likesRepository.likesState,
            ) { playbackState: SmashupPlaybackState,
                nowPlaying: MashupMediaItem?,
                songDuration: Long,
                liked: LikesState ->
                PlayerState(
                    isPlaying = playbackState.rawState.isPlaying,
                    id = nowPlaying?.id ?: 0,
                    isShuffle = playbackState.isShuffle,
                    repeatMode = playbackState.repeatMode,
                    trackDurationMs = songDuration,
                    trackName = nowPlaying?.name ?: "",
                    trackAuthor = nowPlaying?.owner ?: "",
                    isPlaybackNull = nowPlaying == null,
                    coverSmall = _state.value.coverSmall,
                    isLiked = liked.mashupLikes.contains(nowPlaying?.id)
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
                    imageLoader.load(value.id)
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
                delay(200)
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

        // When track have been playing long enough, skipToPrevious does not skip to previous track,
        // but plays current track from start, so we need to call skipToPrevious twice.
        // The 3000 ms threshold is tested empirically
        if (currentTimeMs > 3000) {
            musicService.controls.skipToPrevious()
        }
        musicService.controls.skipToPrevious()
    }

    fun onShuffleClick() {
        musicService.shuffle()
    }

    fun onRepeatClick() {
        musicService.nextRepeatMode()
    }

    fun onLikeClick() {
        val currentlyPlayingId = state.value.id

        if (likesRepository.likesState.value.mashupLikes
                .contains(currentlyPlayingId)) {
            likesRepository.removeLike(currentlyPlayingId)
        } else {
            likesRepository.addLike(currentlyPlayingId)
        }
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
                    .load(ImageUrlHelper.mashupImageIdToUrl100px(state.value.id.toString()))
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
    val id: Int = 0,
    val isPlaying: Boolean = false,
    val isShuffle: Boolean = false,
    val repeatMode: PlaybackRepeatMode = PlaybackRepeatMode.None,
    val trackDurationMs: Long = 148000,
    val trackName: String = "Лобби под подошвой",
    val trackAuthor: String = "Утонул в пиве",
    val isPlaybackNull: Boolean = true,
    val coverSmall: ImageBitmap? = null,
    val isLiked: Boolean = false,
)
