package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.edit_playlist.domain.api.PlaylistsInteractor
import kotlinx.coroutines.launch
import com.example.playlistmaker.edit_playlist.ui.mappers.PlaylistMapper
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.playlist.ui.view_model.model.PlaylistScreenData
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistMapper: PlaylistMapper,
    private val playlistId: Long
) : ViewModel() {

    private val _playlistData: MutableLiveData<PlaylistScreenData> = MutableLiveData()
    private val _playlistDeleteState: MutableLiveData<Boolean> = MutableLiveData()

    val playlistDeleteState: LiveData<Boolean> = _playlistDeleteState
    val playlistData: LiveData<PlaylistScreenData> = _playlistData

    private var ownPlaylist: PlaylistUi? = null

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

    fun deletePlaylist(playlistTracks: List<Track>) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deletePlaylist(playlistId, playlistTracks)
            _playlistDeleteState.postValue(true)
        }
    }

    fun initData() {
        viewModelScope.launch {
            launch {
                playlistsInteractor.getPlaylist(playlistId).collect { playlist ->
                    val receivedPlaylistUi = playlistMapper.map(playlist)
                    ownPlaylist = receivedPlaylistUi
                    _playlistData.value = PlaylistScreenData.GeneralInfo(receivedPlaylistUi)
                }
            }

            launch {
                playlistsInteractor.getPlaylistTracks(playlistId).collect { updatedTrackList ->
                    _playlistData.value = PlaylistScreenData.Tracks(
                        updatedTrackList,
                        computeTotalTime(updatedTrackList),
                        updatedTrackList.size
                    )
                }

            }
        }
    }

    private fun computeTotalTime(trackList: List<Track>): String {
        return SimpleDateFormat("m", Locale.getDefault()).format(trackList.sumOf {
            it.trackTimeMillis
        })
    }
}