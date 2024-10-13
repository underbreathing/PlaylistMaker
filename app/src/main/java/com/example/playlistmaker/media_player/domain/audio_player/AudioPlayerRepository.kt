package com.example.playlistmaker.media_player.domain.audio_player

import kotlinx.coroutines.CoroutineScope

interface AudioPlayerRepository {

    fun preparePlayer(
        dataSource: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun playToggle(statusObserver: StatusObserver, viewModelScope: CoroutineScope)

    fun pausePlayer(onPaused: () -> Unit)


    interface StatusObserver {
        fun onProgress(progress: Int)
        fun onStop()
        fun onPlay()
    }

    fun releasePlayer()


}