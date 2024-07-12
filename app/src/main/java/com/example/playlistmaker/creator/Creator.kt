package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.search.data.impl.NetworkRepositoryImpl
import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.search.data.impl.RemoteDataSourceImpl
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.search.domain.repository.NetworkRepository
import com.example.playlistmaker.search.data.impl.SearchLocalStorageImpl
import com.example.playlistmaker.search.data.impl.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.local_storage.SearchLocalStorage
import com.example.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.search.domain.use_cases.SearchTrackUseCase


object Creator {

    private const val HISTORY_SHARED_PREFERENCES_FILE = "history_shared_preferences_file"

    private lateinit var application: Application

    fun getApplication(): Application {
        return application
    }

    fun initApplicationContext(application: Application) {
        this.application = application
    }

    fun provideTracksHistoryRepository(): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(getLocalStorage())
    }

    fun provideAudioPlayer(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideSearchTrackUseCase(): SearchTrackUseCase {
        return SearchTrackUseCase(getNetworkRepository())
    }

    private fun getNetworkRepository(): NetworkRepository {
        return NetworkRepositoryImpl(RemoteDataSourceImpl(application))
    }

    private fun getLocalStorage(): SearchLocalStorage {
        return SearchLocalStorageImpl(
            application.getSharedPreferences(
                HISTORY_SHARED_PREFERENCES_FILE,
                Context.MODE_PRIVATE
            )
        )
    }
}