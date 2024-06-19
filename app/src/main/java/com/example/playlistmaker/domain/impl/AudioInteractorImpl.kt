package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AudioInteractor
import com.example.playlistmaker.domain.audioplayer.AudioPlayer

class AudioInteractorImpl(private val audioPlayer: AudioPlayer) : AudioInteractor {

    override fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        audioPlayer.preparePlayer(dataSource, onPrepared, onCompletion)
    }

    override fun playControl(onPlayed: () -> Unit, onPaused: () -> Unit) {
        audioPlayer.playControl(onPlayed, onPaused)
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        audioPlayer.pausePlayer(onPaused)
    }

    override fun currentPosition(): Int {
        return audioPlayer.currentPosition()
    }

    override fun releasePlayer() {
        audioPlayer.releasePlayer()
    }
}