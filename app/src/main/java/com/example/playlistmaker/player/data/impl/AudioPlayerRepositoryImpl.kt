package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository

class AudioPlayerRepositoryImpl : MediaPlayer(), AudioPlayerRepository {

    private companion object {
        const val PROGRESS_UPDATE_DELAY = 300L
    }

    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private var playerState = State.DEFAULT
    override fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        this.setDataSource(dataSource)
        this.setOnPreparedListener {
            onPrepared()
            playerState = State.PREPARED
        }
        this.setOnCompletionListener {
            updateProgressTask?.interrupt()
            onCompletion()
            playerState = State.PREPARED
        }
        this.prepareAsync()
    }

    private var updateProgressTask: Thread? = null

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
                updateProgressTask!!.start()
            }

            State.PLAYING -> pausePlayer { statusObserver.onStop() }
            State.DEFAULT -> {}
        }
    }

    private fun updateProgress(statusObserver: AudioPlayerRepository.StatusObserver) {
        statusObserver.onProgress(this.currentPosition)
    }


    private fun startPlayer(onStarted: () -> Unit) {
        this.start()
        playerState = State.PLAYING
        onStarted()
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        this.pause()
        //прерываем задачу обновления трека
        updateProgressTask?.interrupt()
        playerState = State.PAUSED
        onPaused()
    }

    override fun releasePlayer() {
        this.release()
    }

}
