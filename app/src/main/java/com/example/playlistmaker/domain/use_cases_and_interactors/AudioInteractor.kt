package com.example.playlistmaker.domain.use_cases_and_interactors


// как будто можно было бы обойтись без интерфейса для интерактора, но в теории на сайте такой был, поэтому я тоже сделал
interface AudioInteractor {

    fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun playControl(onPlayed: () -> Unit, onPaused: () -> Unit)

    fun pausePlayer(onPaused: () -> Unit)

    fun currentPosition(): Int

    fun releasePlayer()
}