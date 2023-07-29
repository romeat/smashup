package com.romeat.smashup.presentation.home

import androidx.lifecycle.ViewModel
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.musicservice.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class MusicServiceViewModel @Inject constructor(
    protected val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    protected var originalMashupList: List<Mashup> = emptyList()

    fun onMashupClick(mashupId: Int) {
        musicServiceConnection.playMashupFromPlaylist(
            mashupId,
            originalMashupList
        )
    }

    protected fun playCurrentPlaylist(mashupIdToStart: Int, shuffle: Boolean = false) {
        musicServiceConnection.playEntirePlaylist(
            mashupIdToStart = mashupIdToStart,
            playlist = originalMashupList,
            shuffle = shuffle
        )
    }

}

sealed class PlaylistTitle {
    data class StringType(val value: String) : PlaylistTitle()
    data class ResType(val value: Int) : PlaylistTitle()
}