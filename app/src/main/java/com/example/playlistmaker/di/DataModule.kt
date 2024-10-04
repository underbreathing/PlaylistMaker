package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.application.KEY_THEME_FILE
import com.example.playlistmaker.create_playlist.data.mappers.PlaylistEntityMapper
import com.example.playlistmaker.create_playlist.domain.mappers.PlaylistMapper
import com.example.playlistmaker.gson_converter.GsonConverter
import com.example.playlistmaker.media_library.data.db.TrackDatabase
import com.example.playlistmaker.media_library.data.mappers.TrackEntityMapper
import com.example.playlistmaker.search.data.impl.RemoteDataSourceImpl
import com.example.playlistmaker.search.data.impl.SearchLocalStorageImpl
import com.example.playlistmaker.search.data.local_storage.SearchLocalStorage
import com.example.playlistmaker.search.data.mapper.TrackDtoMapper
import com.example.playlistmaker.search.data.remote_data_source.ITunesApi
import com.example.playlistmaker.search.data.remote_data_source.RemoteDataSource
import com.example.playlistmaker.settings.data.impl.SettingsLocalStorageImpl
import com.example.playlistmaker.settings.data.local_storage.SettingsLocalStorage
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val HISTORY_SHARED_PREFERENCES_FILE = "history_shared_preferences_file"

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(), TrackDatabase::class.java, "track_media_library.db"
        ).build()
    }

    single { PlaylistMapper() }
    single { PlaylistEntityMapper() }
    //? single or fabric ?
    single { TrackEntityMapper() }
    single { TrackDtoMapper() }

    single {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ITunesApi::class.java)
    }

    factory { (file: String) ->
        androidContext().getSharedPreferences(file, Context.MODE_PRIVATE)
    }

    single {
        GsonConverter(Gson())
    }

    factory {
        MediaPlayer()
    }

    single<SearchLocalStorage> {
        SearchLocalStorageImpl(get { parametersOf(HISTORY_SHARED_PREFERENCES_FILE) })
    }

    single<RemoteDataSource> {
        RemoteDataSourceImpl(androidContext(), get())
    }

    single<SettingsLocalStorage> {
        SettingsLocalStorageImpl(get { parametersOf(KEY_THEME_FILE) })
    }


}