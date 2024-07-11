package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository

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

    private var updateProgressTask: Thread? = null
    private var playerState = State.DEFAULT
    override fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        player.setDataSource(dataSource)
        player.setOnPreparedListener {
            onPrepared()
            playerState = State.PREPARED
        }
        player.setOnCompletionListener {
            updateProgressTask?.interrupt()
            onCompletion()
            playerState = State.PREPARED
        }
        player.prepareAsync()
    }

    override fun playToggle(statusObserver: AudioPlayerRepository.StatusObserver) {
        when (playerState) {
            State.PREPARED, State.PAUSED -> {
                startPlayer { statusObserver.onPlay() }
                //запускаем обновление прогресса
                updateProgressTask = Thread {
                    while (!Thread.interrupted()) {
                        try {
                            Thread.sleep(PROGRESS_UPDATE_DELAY) // обработать возможное исключение?
                        } catch (exception: InterruptedException) {
                            break
                        }
                        updateProgress(statusObserver)
                    }
                }
                updateProgressTask?.start()
            }

            State.PLAYING -> pausePlayer { statusObserver.onStop() }
            State.DEFAULT -> {}
        }
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        player.pause()
        //прерываем задачу обновления трека
        updateProgressTask?.interrupt()
        playerState = State.PAUSED
        onPaused()
    }

    override fun releasePlayer() {
        player.release()
    }

    private fun updateProgress(statusObserver: AudioPlayerRepository.StatusObserver) {
        statusObserver.onProgress(player.currentPosition)
    }

    private fun startPlayer(onStarted: () -> Unit) {
        player.start()
        playerState = State.PLAYING
        onStarted()
    }
}
