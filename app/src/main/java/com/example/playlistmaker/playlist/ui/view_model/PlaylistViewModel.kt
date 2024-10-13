package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import kotlinx.coroutines.launch
import com.example.playlistmaker.create_playlist.ui.mappers.PlaylistMapper
import com.example.playlistmaker.playlist.ui.PlaylistCompleteDataUi
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor, private val playlistMapper: PlaylistMapper
) : ViewModel() {

    private val _receivedPlaylist: MutableLiveData<PlaylistCompleteDataUi?> = MutableLiveData()

    val receivedPlaylist: LiveData<PlaylistCompleteDataUi?> get() = _receivedPlaylist


    fun initData(playlistId: Long) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylist(playlistId).collect { playlist ->
                playlist?.let {
                    var totalTime = "0"
                    playlistsInteractor.getPlaylistTracks(it.trackIds).collect { trackList ->
                        if (trackList.isNotEmpty()) {
                            totalTime = computeTotalTime(trackList)
                        }
                        _receivedPlaylist.value =
                            PlaylistCompleteDataUi(
                                playlistMapper.map(playlist),
                                totalTime,
                                trackList
                            )
                    }
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