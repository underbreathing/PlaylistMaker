package com.example.playlistmaker.creator

import android.app.Activity
import android.content.Context
import com.example.playlistmaker.search.data.impl.NetworkRepositoryImpl
import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.data.impl.InternalNavigatorImpl
import com.example.playlistmaker.search.data.impl.RemoteDataSourceImpl
import com.example.playlistmaker.player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.player.domain.internal_navigator.InternalNavigator
import com.example.playlistmaker.search.domain.repository.NetworkRepository
import com.example.playlistmaker.search.data.impl.SearchLocalStorageImpl
import com.example.playlistmaker.search.data.impl.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.local_storage.SearchLocalStorage
import com.example.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.search.domain.use_cases.SearchTrackUseCase
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private const val HISTORY_SHARED_PREFERENCES_FILE = "history_shared_preferences_file"

    fun provideInternalNavigator(activity: Activity): InternalNavigator {
        return InternalNavigatorImpl(activity)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideTracksHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(getLocalStorage(context))
    }

    private fun getLocalStorage(context: Context): SearchLocalStorage {
        return SearchLocalStorageImpl(
            context.getSharedPreferences(
                HISTORY_SHARED_PREFERENCES_FILE,
                Context.MODE_PRIVATE
            )
        )
    }

    fun provideAudioPlayer(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideSearchTrackUseCase(context: Context): SearchTrackUseCase {
        return SearchTrackUseCase(getNetworkRepository(context))
    }

    private fun getNetworkRepository(context: Context): NetworkRepository {
        return NetworkRepositoryImpl(RemoteDataSourceImpl(context))
    }
}