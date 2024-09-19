package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.player.ui.view_model.model.PlayStatus

class MediaPlayerViewModel(
    private val audioPlayerRepository: AudioPlayerRepository, dataSource: String?
) : ViewModel() {

    init {
        Log.d("MYPLAYER", "view model init")
        if (dataSource?.isNotEmpty() == true) {
            preparePlayer(dataSource)
        }
    }

    private val playStatusLiveData: MutableLiveData<PlayStatus> = MutableLiveData()
    private val mediaPlayerStateLiveData: MutableLiveData<MediaPlayerState> = MutableLiveData()

    enum class MediaPlayerState {
        READY, COMPLETED
    }

    override fun onCleared() {
        audioPlayerRepository.releasePlayer()
        super.onCleared()
    }

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer {
            playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
        }
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    fun getMediaPlayerStateLiveData(): LiveData<MediaPlayerState> = mediaPlayerStateLiveData

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
}