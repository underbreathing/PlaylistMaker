package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import kotlinx.coroutines.launch
import com.example.playlistmaker.create_playlist.ui.mappers.PlaylistMapper
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.playlist.ui.model.PlaylistCompleteDataUi
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistMapper: PlaylistMapper,
    playlistId: Long
) : ViewModel() {

    private val _receivedPlaylist: MutableLiveData<PlaylistCompleteDataUi?> = MutableLiveData()

    init {
        initData(playlistId)
    }

    val receivedPlaylist: LiveData<PlaylistCompleteDataUi?> get() = _receivedPlaylist

    private var ownPlaylist: PlaylistUi? = null


    private fun initData(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            ownPlaylist = playlistMapper.map(playlistsInteractor.getPlaylist(playlistId))
            var totalTime: String
            playlistsInteractor.getPlaylistTracks(ownPlaylist?.trackIds ?: emptyList())
                .collect { updatedTrackList ->
                    totalTime = computeTotalTime(updatedTrackList)
                    _receivedPlaylist.postValue(
                        PlaylistCompleteDataUi(
                            ownPlaylist, totalTime, updatedTrackList
                        )
                    )
                }
        }
    }

    private fun computeTotalTime(trackList: List<Track>): String {
        return SimpleDateFormat("m", Locale.getDefault()).format(trackList.sumOf {
            it.trackTimeMillis
        })
    }

    fun deleteTrack(track: Track) {
        ownPlaylist?.let { playlist ->
            if (playlist.trackIds.isNotEmpty()) {
                val newOwn = playlist.copy(
                    trackIds = playlist.trackIds.toMutableList().apply { remove(track.trackId) },
                    trackCount = playlist.trackCount - 1
                )
                ownPlaylist = newOwn
                viewModelScope.launch(Dispatchers.IO) {
                    playlistsInteractor.deleteTrackFromPlaylist(playlistMapper.map(newOwn), track)
                }
            }
        }
    }

}