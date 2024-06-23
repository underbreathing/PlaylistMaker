package com.example.playlistmaker.domain.use_cases_and_interactors

import com.example.playlistmaker.domain.audioplayer.AudioPlayerRepository

class AudioInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioInteractor {

    override fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        audioPlayerRepository.preparePlayer(dataSource, onPrepared, onCompletion)
    }

    override fun playControl(onPlayed: () -> Unit, onPaused: () -> Unit) {
        audioPlayerRepository.playControl(onPlayed, onPaused)
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        audioPlayerRepository.pausePlayer(onPaused)
    }

    override fun currentPosition(): Int {
        return audioPlayerRepository.currentPosition()
    }

    override fun releasePlayer() {
        audioPlayerRepository.releasePlayer()
    }
}