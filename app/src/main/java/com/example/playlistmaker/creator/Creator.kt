package com.example.playlistmaker.creator

import com.example.playlistmaker.data.audio_player.AudioPlayerImpl
import com.example.playlistmaker.domain.api.AudioInteractor
import com.example.playlistmaker.domain.audioplayer.AudioPlayer
import com.example.playlistmaker.domain.impl.AudioInteractorImpl

object Creator {

    fun provideAudioInteractor(): AudioInteractor{
        return AudioInteractorImpl(getAudioPlayerClient())
    }

    private fun getAudioPlayerClient(): AudioPlayer{
        return AudioPlayerImpl()
    }
}