package com.example.playlistmaker.create_playlist.domain.api

import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import kotlinx.coroutines.flow.Flow


interface PlaylistsInteractor {

    suspend fun savePlaylist(playlist: Playlist)

    fun getPlayLists(): Flow<List<PlaylistInfo>>
}