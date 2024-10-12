package com.example.playlistmaker.di


import com.example.playlistmaker.create_playlist.ui.CreatePlaylistViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.media_library.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.media_player.ui.view_model.MediaPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        CreatePlaylistViewModel(get(), get())
    }

    viewModel { (dataSource: String?, track: Track) ->
        MediaPlayerViewModel(get(), dataSource, get(), track, get())
    }

    viewModel {
        SearchViewModel(androidApplication(), get(), get())
    }

    viewModel {
        SettingsViewModel(androidApplication(), get())
    }

    viewModel {
        PlaylistsFragmentViewModel(get())
    }

    viewModel {
        FavoriteTracksViewModel(get(), androidContext())
    }


}