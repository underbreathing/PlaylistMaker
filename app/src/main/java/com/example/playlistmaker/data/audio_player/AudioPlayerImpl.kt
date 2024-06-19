package com.example.playlistmaker.data.audio_player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.audioplayer.AudioPlayer

class AudioPlayerImpl : MediaPlayer(), AudioPlayer {
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
            onCompletion()
            playerState = State.PREPARED
        }
        this.prepareAsync()
    }

    override fun playControl(onPlayed: () -> Unit, onPaused: () -> Unit) {
        when (playerState) {
            State.PREPARED, State.PAUSED -> startPlayer(onPlayed)
            State.PLAYING -> pausePlayer(onPaused)
            State.DEFAULT -> {}
        }
    }

    private fun startPlayer(onStarted: () -> Unit) {
        this.start()
        playerState = State.PLAYING
        onStarted()
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        this.pause()
        playerState = State.PAUSED
        onPaused()
    }

    override fun currentPosition(): Int {
        return this.currentPosition
    }

    override fun releasePlayer() {
        this.release()
    }

}
