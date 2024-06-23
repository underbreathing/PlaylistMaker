package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.repository.NetworkRepositoryImpl
import com.example.playlistmaker.data.audio_player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.remote_data_source.RemoteDataSourceImpl
import com.example.playlistmaker.domain.use_cases_and_interactors.AudioInteractor
import com.example.playlistmaker.domain.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.domain.repository.NetworkRepository
import com.example.playlistmaker.domain.use_cases_and_interactors.AudioInteractorImpl
import com.example.playlistmaker.domain.use_cases_and_interactors.SearchTrackUseCase

object Creator {

    fun provideAudioInteractor(): AudioInteractor {
        return AudioInteractorImpl(getAudioPlayerClient())
    }

    private fun getAudioPlayerClient(): AudioPlayerRepository{
        return AudioPlayerRepositoryImpl()
    }

    fun provideSearchTrackUseCase(context: Context): SearchTrackUseCase{
        return SearchTrackUseCase(getNetworkRepository(context))
    }

    private fun getNetworkRepository(context: Context): NetworkRepository{
        return NetworkRepositoryImpl(RemoteDataSourceImpl(context))
    }
}