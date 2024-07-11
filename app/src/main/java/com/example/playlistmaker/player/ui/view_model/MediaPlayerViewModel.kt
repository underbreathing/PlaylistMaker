package com.example.playlistmaker.player.ui.view_model

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.player.ui.view_model.model.PlayStatus

class MediaPlayerViewModel(
    private val audioPlayerRepository: AudioPlayerRepository, dataSource: String?
) : ViewModel() {

    init {
        Log.d("MYPLAYER","view model init")
        if (dataSource?.isNotEmpty() == true) {
            preparePlayer(dataSource)
        }
    }

    companion object {
        fun getMediaPlayerViewModelFactory(dataSource: String?): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MediaPlayerViewModel(
                        Creator.provideAudioPlayer(), dataSource
                    )
                }
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
    fun preparePlayer(dataSource: String) {
        audioPlayerRepository.preparePlayer(dataSource, {
            mediaPlayerStateLiveData.postValue(MediaPlayerState.READY)
        }, {
            mediaPlayerStateLiveData.postValue(MediaPlayerState.COMPLETED)
        })
    }

    fun playToggle() {
        audioPlayerRepository.playToggle(object : AudioPlayerRepository.StatusObserver {
            override fun onProgress(progress: Int) {
                //используем post, т.к это вызывается в отдельном потоке в AudioPlayerRepositoryImpl
                playStatusLiveData.postValue(getCurrentPlayStatus().copy(progress = progress))
            }

            override fun onStop() {
                playStatusLiveData.postValue(getCurrentPlayStatus().copy(isPlaying = false))

            }

            override fun onPlay() {
                playStatusLiveData.postValue(getCurrentPlayStatus().copy(isPlaying = true))
            }
        })
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(false, 0)
    }
}