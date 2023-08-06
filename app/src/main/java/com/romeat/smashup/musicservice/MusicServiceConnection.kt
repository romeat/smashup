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
import com.romeat.smashup.data.dto.MashupMediaItem
import com.romeat.smashup.musicservice.mapper.MediaMetadataMapper
import com.romeat.smashup.presentation.home.PlaylistTitle
import com.romeat.smashup.util.MediaConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

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
    private val _nowPlayingMashup = MutableStateFlow<MashupMediaItem?>(null)
    val nowPlayingMashup: StateFlow<MashupMediaItem?> = _nowPlayingMashup

    // shows playlist title
    private val _playlistTitle = MutableStateFlow<String>("")
    val playlistTitle = _playlistTitle.asStateFlow()

    private val _nowPlayingQueue = MutableStateFlow(emptyList<MediaSessionCompat.QueueItem>())
    val nowPlayingQueue: StateFlow<List<MediaSessionCompat.QueueItem>> = _nowPlayingQueue

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

    private fun getStringTitle(title: PlaylistTitle): String {
        return when (title) {
            is PlaylistTitle.StringType -> title.value
            is PlaylistTitle.ResType -> context.getString(title.value)
        }
    }

    fun playMashupFromPlaylist(
        mashupIdToStart: Int,
        playlist: List<Mashup>,
        title: PlaylistTitle,
    ) {
        _playlistTitle.value = getStringTitle(title)
        val nowPlaying = nowPlayingMashup.value
        nowPlaying?.id.let { nowPlayingId ->
            if (nowPlayingId == mashupIdToStart) {
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
                extras.putString(MediaConstants.PLAYLIST_TO_PLAY, Json.encodeToString(playlist))
                controls.playFromMediaId(mashupIdToStart.toString(), extras)
            }
        }
    }

    fun playEntirePlaylist(
        mashupIdToStart: Int,
        playlist: List<Mashup>,
        title: PlaylistTitle,
        shuffle: Boolean = false
    ) {
        _playlistTitle.value = getStringTitle(title)
        val extras = Bundle()
        extras.putString(MediaConstants.PLAYLIST_TO_PLAY, Json.encodeToString(playlist))

        controls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)

        if (!shuffle) {
            controls.playFromMediaId(mashupIdToStart.toString(), extras)
        } else {
            job?.cancel()

            // The problem with shuffle is that we cant just say "shuffle media queue then play from first media item in new queue",
            // we must always specify song id to start from. But if we call .playFromMediaId() with some id, then shuffle,
            // playback will start from that id, not from the start of shuffled list. We need to handle it.

            // So, when we click shuffle button, player loads all media items to media queue with normal order
            // and adds inner queueIds starting from 0.
            controls.prepareFromMediaId(mashupIdToStart.toString(), extras)
            // Then we set Shuffle mode:
            controls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
            // After that player rearranges media items randomly and updates media queue
            // (so order of queueIds change, for example, from [0, 1, 2] to [2, 0, 1])

            // This coroutine checks media queue for updates from callback,
            // and when first item of media queue is not the same as first item of playlist parameter,
            // that means media queue was shuffled, and we need to manually start playback from first item
            // by calling .skipToQueueItem()
            job = scope.launch {
                while (true) {
                    delay(20)
                    val list = nowPlayingQueue.value

                    if (list.isNotEmpty() && list.first().description.mediaId != playlist.first().id.toString()) {
                        controls.skipToQueueItem(list.first().queueId)
                        return@launch
                    }
                }
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
            Log.e("TAG", "Player error: onConnectionFailed");
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
                    repeatMode = when (mediaController.repeatMode) {
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
                _currentSongDuration.value = metadata.duration
                _nowPlayingMashup.value = MediaMetadataMapper.convertFromMedia(metadata)
            }
        }

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            _nowPlayingQueue.value = queue ?: emptyList()
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