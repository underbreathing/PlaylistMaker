package com.example.playlistmaker.domain.audioplayer

interface AudioPlayerRepository {

    fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    // Функция обработки нажатия на кнопку play,
    // принимает функции, которые в слое presentation по новому состоянию делают соотв. изменения в ui
    fun playControl(onPlayed: () -> Unit, onPaused: () -> Unit)

    fun pausePlayer(onPaused: () -> Unit)

    fun currentPosition(): Int

    fun releasePlayer()
}