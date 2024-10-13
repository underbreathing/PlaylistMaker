package com.example.playlistmaker.media_player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.media_player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import com.example.playlistmaker.media_library.ui.view_model.state.PlaylistsDataState
import com.example.playlistmaker.media_player.ui.view_model.model.PlayStatus
import com.example.playlistmaker.media_player.ui.view_model.model.TrackAddState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val audioPlayerRepository: AudioPlayerRepository,
    private val dataSource: String?,
    private val mediaLibraryRepository: MediaLibraryRepository,
    private val track: Track,
    private val playlistsInteractor: PlaylistsInteractor

) : ViewModel() {

    private val playStatusLiveData: MutableLiveData<PlayStatus> = MutableLiveData()
    private val mediaPlayerStateLiveData: MutableLiveData<MediaPlayerState> = MutableLiveData()
    private val trackInMediaLibraryLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val playlistsLiveData: MutableLiveData<PlaylistsDataState> = MutableLiveData()
    private var addTrackToPlaylistState: MutableLiveData<TrackAddState> = MutableLiveData()
    init {
        fillPlaylists()
    }

    enum class MediaPlayerState {
        READY, COMPLETED
    }

    override fun onCleared() {
        audioPlayerRepository.releasePlayer()
        super.onCleared()
    }

    fun onCreateView() {
        if (!dataSource.isNullOrEmpty()) {
            preparePlayer(dataSource)
        }
        checkIsTrackInMediaLibrary(track.trackId)
    }

    fun addTrackToPlaylist(playlist: PlaylistInfo) {
        val trackId = track.trackId
        val trackIds = playlist.trackIds
        if (trackIds.isEmpty()) {
            updatePlaylist(
                playlist.copy(
                    trackIds = listOf(trackId),
                    trackCount = 1
                ),
                track
            )
        } else {
            if (trackIds.contains(trackId)) {
                addTrackToPlaylistState.postValue(TrackAddState(playlist.title, false))
            } else {
                updatePlaylist(
                    playlist.copy(
                        trackIds = trackIds + trackId,
                        trackCount = playlist.trackCount + 1
                    ),
                    track
                )
            }
        }
    }


    private fun updatePlaylist(playlist: PlaylistInfo, track: Track) {
        viewModelScope.launch {
            playlistsInteractor.addTrackToPlaylist(playlist, track)
            addTrackToPlaylistState.postValue(TrackAddState(playlist.title, true))
        }
    }


    fun observeAddTrackToPlaylistState(): LiveData<TrackAddState> {
        return addTrackToPlaylistState
    }

    fun observePlaylistLiveData(): LiveData<PlaylistsDataState> = playlistsLiveData

    fun saveTrackToMediaLibrary(track: Track, additionTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaLibraryRepository.putToMediaLibrary(track, additionTime)
            trackInMediaLibraryLiveData.postValue(true)
        }
    }

    fun observeTrackInMediaLibrary(): LiveData<Boolean> = trackInMediaLibraryLiveData


    fun pausePlayer() {
        Log.d("PLAYER", "pause player")
        audioPlayerRepository.pausePlayer {
            playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
        }
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    fun getMediaPlayerStateLiveData(): LiveData<MediaPlayerState> = mediaPlayerStateLiveData

    fun deleteFromMediaLibrary(trackId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaLibraryRepository.deleteFromMediaLibrary(trackId)
            trackInMediaLibraryLiveData.postValue(false)
        }
    }

    fun playToggle() {
        audioPlayerRepository.playToggle(
            object : AudioPlayerRepository.StatusObserver {
                override fun onProgress(progress: Int) {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onStop() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)

                }

                override fun onPlay() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                }
            }, viewModelScope
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(false, 0)
    }

    private fun preparePlayer(dataSource: String) {
        audioPlayerRepository.preparePlayer(dataSource, {
            mediaPlayerStateLiveData.postValue(MediaPlayerState.READY)
        }, {
            mediaPlayerStateLiveData.postValue(MediaPlayerState.COMPLETED)
        })
    }

    private fun checkIsTrackInMediaLibrary(trackId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaLibraryRepository.isTrackInMediaLibrary(trackId).collect {
                trackInMediaLibraryLiveData.postValue(it)
            }
        }
    }

    private fun fillPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlayLists().collect { data ->
                if (data.isEmpty()) {
                    playlistsLiveData.postValue(PlaylistsDataState.Empty)
                } else {
                    playlistsLiveData.postValue(PlaylistsDataState.Content(data))
                }
            }
        }
    }

    fun onDestroy() {
        addTrackToPlaylistState = MutableLiveData()
    }
}