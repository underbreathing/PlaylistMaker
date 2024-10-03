package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.ViewModel

import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor

class PlaylistsFragmentViewModel(private val playlistsInteractor: PlaylistsInteractor): ViewModel() {

    private fun fillData(){
        playlistsInteractor.getPlayLists()
    }

}