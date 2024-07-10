package com.example.playlistmaker.player.domain.audio_player

interface AudioPlayerRepository {

    fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun playToggle(statusObserver: StatusObserver)

    fun pausePlayer(onPaused: () -> Unit)


    interface StatusObserver {
        fun onProgress(progress: Int)
        fun onStop()
        fun onPlay()
    }

    fun releasePlayer()


}