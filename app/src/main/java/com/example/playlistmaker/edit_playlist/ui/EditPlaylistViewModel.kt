package com.example.playlistmaker.edit_playlist.ui

import com.example.playlistmaker.edit_playlist.domain.api.LocalFileStorage
import com.example.playlistmaker.edit_playlist.domain.api.PlaylistsInteractor

class EditPlaylistViewModel(
    private val localFileStorage: LocalFileStorage,
    private val playlistsInteractor: PlaylistsInteractor
) : CreatePlaylistViewModel(localFileStorage, playlistsInteractor) {

}