package com.romeat.smashup.musicservice

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupUiData
import com.romeat.smashup.musicservice.mapper.MediaMetadataMapper
import com.romeat.smashup.util.MediaConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _networkFailure = MutableStateFlow(false)
    val networkFailure: StateFlow<Boolean> = _networkFailure


    val controls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    // shows status - playing or not, shuffle and repeat mode
    private val _playbackState = MutableStateFlow(SmashupPlaybackState())
    val playbackState: StateFlow<SmashupPlaybackState> = _playbackState

    // shows what exactly is playing now
    private val _nowPlayingMashup = MutableStateFlow<MashupUiData?>(null)
    val nowPlayingMashup: StateFlow<MashupUiData?> = _nowPlayingMashup

    // currently playing playlist
    private val _nowPlayingPlaylist = MutableStateFlow(emptyList<MashupUiData>())
    val nowPlayingPlaylist: StateFlow<List<MashupUiData>> = _nowPlayingPlaylist

    // tracks song duration
    private val _currentSongDuration = MutableStateFlow(-1L)
    val currentSongDuration: StateFlow<Long> = _currentSongDuration

    private val serviceComponent = ComponentName(context, MusicService::class.java)

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponent,
        mediaBrowserConnectionCallback, null
    ).apply { connect() }
    private lateinit var mediaController: MediaControllerCompat


    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun getTime(): Long {
        return try {
            mediaController.playbackState.currentPlayBackPosition
        } catch (e: Exception) {
            0L
        }
    }

    fun playMashupFromPlaylist(
        mashupToStart: Mashup,
        playlist: List<Mashup>,
        shuffle: Boolean = false
    ) {
        val nowPlaying = nowPlayingMashup.value
        nowPlaying?.id.let { nowPlayingId ->
            if (nowPlayingId == mashupToStart.id) {
                playbackState.value.let { playbackState ->
                    when {
                        playbackState.rawState.isPlaying ->
                            controls.pause()
                        playbackState.rawState.isPlayEnabled -> controls.play()
                        else -> {}
                    }
                }
            } else {
                val extras = Bundle()
                extras.putString(MediaConstants.MASHUP_TO_PLAY, Json.encodeToString(mashupToStart))
                extras.putString(MediaConstants.PLAYLIST_TO_PLAY, Json.encodeToString(playlist))
                controls.playFromMediaId(mashupToStart.id.toString(), extras)
            }
        }
    }

    fun shuffle() {
        if (mediaController.shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE) {
            controls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
        } else {
            controls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
        }
    }

    fun nextRepeatMode() {
        // the cycle: no repeat -> repeat song -> repeat playlist -> no repeat ->...
        when (mediaController.repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE ->
                controls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
            PlaybackStateCompat.REPEAT_MODE_ALL ->
                controls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
            else ->
                controls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
        }
    }

    fun sendCommand(command: String, parameters: Bundle?) =
        sendCommand(command, parameters) { _, _ -> }

    fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ((Int, Bundle?) -> Unit)
    ) = if (mediaBrowser.isConnected) {
        mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                resultCallback(resultCode, resultData)
            }
        })
        true
    } else {
        false
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context) :
        MediaBrowserCompat.ConnectionCallback() {
        /**
         * Invoked after [MediaBrowserCompat.connect] when the request has successfully
         * completed.
         */
        override fun onConnected() {
            // Get a MediaController for the MediaSession.
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.value = true
        }

        /**
         * Invoked when the client is disconnected from the media browser.
         */
        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        /**
         * Invoked when the connection to the media browser failed.
         */
        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            if (state == null) {
                _playbackState.value = SmashupPlaybackState()
            } else {
                _playbackState.value = SmashupPlaybackState(
                    rawState = state,
                    isShuffle = mediaController.shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL
                            || mediaController.shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_GROUP,
                    repeatMode = when(mediaController.repeatMode) {
                        PlaybackStateCompat.REPEAT_MODE_ONE -> PlaybackRepeatMode.RepeatOneSong
                        PlaybackStateCompat.REPEAT_MODE_ALL -> PlaybackRepeatMode.RepeatPlaylist
                        else -> PlaybackRepeatMode.None
                    }
                )
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            // When ExoPlayer stops we will receive a callback with "empty" metadata. This is a
            // metadata object which has been instantiated with default values. The default value
            // for media ID is null so we assume that if this value is null we are not playing
            // anything.
            if (metadata?.id == null) {
                _nowPlayingMashup.value = null
                _currentSongDuration.value = -1
            } else {
                _nowPlayingMashup.value = MediaMetadataMapper.convertFromMedia(metadata)
                _currentSongDuration.value = metadata.duration
            }
        }

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            // TODO check if it is actually playlist
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_FAILURE -> _networkFailure.value = true
            }
        }

        /**
         * Normally if a [MediaBrowserServiceCompat] drops its connection the callback comes via
         * [MediaControllerCompat.Callback] (here). But since other connection status events
         * are sent to [MediaBrowserCompat.ConnectionCallback], we catch the disconnect here and
         * send it on to the other callback.
         */
        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}

data class SmashupPlaybackState(
    val rawState: PlaybackStateCompat = EMPTY_PLAYBACK_STATE,
    val isShuffle: Boolean = false,
    val repeatMode: PlaybackRepeatMode = PlaybackRepeatMode.None
)

sealed class PlaybackRepeatMode {
    object None : PlaybackRepeatMode()
    object RepeatOneSong : PlaybackRepeatMode()
    object RepeatPlaylist : PlaybackRepeatMode()
}


@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

@Suppress("PropertyName")
val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()