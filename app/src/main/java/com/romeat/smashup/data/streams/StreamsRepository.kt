package com.romeat.smashup.data.streams

import com.romeat.smashup.BuildConfig
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.network.SmashupRemoteData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamsRepository @Inject constructor(
    val musicServiceConnection: MusicServiceConnection,
    private val remoteData: SmashupRemoteData
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var addStreamJob: Job? = null

    init {
        scope.launch {
            musicServiceConnection
                .nowPlayingMashup
                .collect { media ->
                    addStreamJob?.cancel()
                    if (media != null) {
                        delay(100)
                        addStreamJob = scope.launch {
                            delay(minOf(15000L, musicServiceConnection.currentSongDuration.value / 2000L))
                            if (isActive && !BuildConfig.DEBUG) {
                                addStreamToMashup()
                            }
                        }
                    }
                }
        }
    }


    private suspend fun addStreamToMashup() {
        try {
            musicServiceConnection.nowPlayingMashup.value?.id?.let {
                remoteData.addStreamToMashup(it)
            }
        } catch (e: Exception) {

        }
    }
}