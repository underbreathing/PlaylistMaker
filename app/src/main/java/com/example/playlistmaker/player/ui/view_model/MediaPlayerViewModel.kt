package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import com.example.playlistmaker.media_library.ui.view_model.state.PlaylistsDataState
import com.example.playlistmaker.player.ui.view_model.model.PlayStatus
import com.example.playlistmaker.player.ui.view_model.model.TrackAddState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val audioPlayerRepository: AudioPlayerRepository, private val dataSource: String?,
    private val mediaLibraryRepository: MediaLibraryRepository,
    private val trackId: Long,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

//    init {
//        Log.d("MYPLAYER", "view model init")
//        if (!dataSource.isNullOrEmpty()) {
//            preparePlayer(dataSource)
//        }
//        checkIsTrackInMediaLibrary(trackId)
//    }

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

//    fun onDestroy(){
//        audioPlayerRepository.releasePlayer()
//    }

    fun onCreateView(){
        if (!dataSource.isNullOrEmpty()) {
            preparePlayer(dataSource)
        }
        checkIsTrackInMediaLibrary(trackId)
    }

    fun addTrackToPlaylist(playlist: PlaylistInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (playlistsInteractor.addTrackToPlaylist(trackId, playlist.id)) {
                addTrackToPlaylistState.postValue(TrackAddState(playlist.title, true))
            } else {
                addTrackToPlaylistState.postValue(TrackAddState(playlist.title, false))
            }
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
            },
            viewModelScope
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