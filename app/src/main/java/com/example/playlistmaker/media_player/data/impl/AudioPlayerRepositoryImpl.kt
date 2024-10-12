package com.example.playlistmaker.media_player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.media_player.domain.audio_player.AudioPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerRepositoryImpl(private val player: MediaPlayer) : AudioPlayerRepository {

    private companion object {
        const val PROGRESS_UPDATE_DELAY = 300L
    }

    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private var updateProgressJob: Job? = null
    private var playerState = State.DEFAULT
    override fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        player.reset()
        player.setDataSource(dataSource)
        player.setOnPreparedListener {
            onPrepared()
            playerState = State.PREPARED
        }
        player.setOnCompletionListener {
            updateProgressJob?.cancel()
            onCompletion()
            playerState = State.PREPARED
        }
        player.prepareAsync()
    }

    override fun playToggle(
        statusObserver: AudioPlayerRepository.StatusObserver,
        viewModelScope: CoroutineScope
    ) {
        when (playerState) {
            State.PREPARED, State.PAUSED -> {
                startPlayer { statusObserver.onPlay() }
                //запускаем обновление прогресса
                startTimer(viewModelScope) { progress: Int ->
                    statusObserver.onProgress(progress)
                }
            }

            State.PLAYING -> pausePlayer { statusObserver.onStop() }
            State.DEFAULT -> {}
        }
    }

    private fun startTimer(
        scope: CoroutineScope,
        onProgress: (Int) -> Unit
    ) {
        updateProgressJob = scope.launch {
            while (true) {
                delay(PROGRESS_UPDATE_DELAY)
                onProgress(player.currentPosition)
            }
        }
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        player.pause()
        //прерываем задачу обновления трека
        updateProgressJob?.cancel()
        playerState = State.PAUSED
        onPaused()
    }

    override fun releasePlayer() {
        player.release()
    }

    private fun startPlayer(onStarted: () -> Unit) {
        player.start()
        playerState = State.PLAYING
        onStarted()
    }
}
