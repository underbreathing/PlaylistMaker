package com.example.playlistmaker.player.ui.view_model

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.player.domain.internal_navigator.InternalNavigator
import com.example.playlistmaker.player.ui.view_model.model.PlayStatus
import com.example.playlistmaker.search.domain.model.Track

class MediaPlayerViewModel(
    private val audioPlayerRepository: AudioPlayerRepository,
    private val internalNavigator: InternalNavigator
) : ViewModel() {

    enum class MediaPlayerState {
        READY, COMPLETED
    }

    override fun onCleared() {
        audioPlayerRepository.releasePlayer()
        super.onCleared()
    }

    fun getArrivedTrack(): Track? {
        return internalNavigator.getArrivedTrack()
    }

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer {
            playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
        }
    }


    private val playStatusLiveData: MutableLiveData<PlayStatus> = MutableLiveData()

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(false, 0)
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    private val mediaPlayerStateLiveData: MutableLiveData<MediaPlayerState> = MutableLiveData()
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

    companion object {
        fun getMediaPlayerViewModelFactory(activity: Activity): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MediaPlayerViewModel(
                        Creator.provideAudioPlayer(), Creator.provideInternalNavigator(activity)
                    )
                }
            }
    }
}