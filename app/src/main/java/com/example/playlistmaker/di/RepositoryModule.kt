package com.example.playlistmaker.di

import com.example.playlistmaker.create_playlist.data.impl.LocalFileStorageImpl
import com.example.playlistmaker.create_playlist.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.create_playlist.domain.api.LocalFileStorage
import com.example.playlistmaker.create_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.media_player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.media_library.data.impl.MediaLibraryRepositoryImpl
import com.example.playlistmaker.media_player.domain.audio_player.AudioPlayerRepository
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import com.example.playlistmaker.search.data.impl.NetworkRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.NetworkRepository
import com.example.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    factory<LocalFileStorage> {
        LocalFileStorageImpl(androidContext())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<MediaLibraryRepository> {
        MediaLibraryRepositoryImpl(get(), get(),get())
    }

    single<NetworkRepository> {
        NetworkRepositoryImpl(get(), get())
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidApplication(), get())
    }

}