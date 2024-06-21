package com.example.playlistmaker.creator

import com.example.playlistmaker.data.NetworkRepositoryImpl
import com.example.playlistmaker.data.audio_player.AudioPlayerImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.AudioInteractor
import com.example.playlistmaker.domain.audioplayer.AudioPlayer
import com.example.playlistmaker.domain.repository.NetworkRepository
import com.example.playlistmaker.domain.use_cases_and_interactors.AudioInteractorImpl
import com.example.playlistmaker.domain.use_cases_and_interactors.SearchTrackUseCase

object Creator {

    fun provideAudioInteractor(): AudioInteractor{
        return AudioInteractorImpl(getAudioPlayerClient())
    }

    private fun getAudioPlayerClient(): AudioPlayer{
        return AudioPlayerImpl()
    }

    fun provideSearchTrackUseCase(): SearchTrackUseCase{
        return SearchTrackUseCase(getNetworkRepository())
    }

    private fun getNetworkRepository(): NetworkRepository{
        return NetworkRepositoryImpl(RetrofitNetworkClient())
    }
}