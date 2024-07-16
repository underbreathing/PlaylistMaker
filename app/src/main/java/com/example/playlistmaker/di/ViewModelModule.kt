package com.example.playlistmaker.di


import com.example.playlistmaker.media_library.ui.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.media_library.ui.view_model.SelectedTracksFragmentViewModel
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (dataSource: String?) ->
        MediaPlayerViewModel(get(), dataSource)
    }

    viewModel {
        SearchViewModel(androidApplication(), get(), get())
    }

    viewModel {
        SettingsViewModel(androidApplication(), get())
    }

    viewModel {
        PlaylistsFragmentViewModel()
    }

    viewModel {
        SelectedTracksFragmentViewModel()
    }


}