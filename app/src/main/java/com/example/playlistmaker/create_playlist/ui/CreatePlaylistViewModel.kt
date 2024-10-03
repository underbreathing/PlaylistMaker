package com.example.playlistmaker.create_playlist.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.LocalFileStorage
import com.example.playlistmaker.create_playlist.domain.PlaylistRepository
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreatePlaylistViewModel(
    private val localFileStorage: LocalFileStorage,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {


    fun savePlaylist(title: String, description: String?, coverUri: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.savePlaylist(
                Playlist(
                    title,
                    description,
                    coverUri?.toString(),
                    null,
                    0
                )
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            coverUri?.let { localFileStorage.saveImage(it, title) }
        }

    }

}