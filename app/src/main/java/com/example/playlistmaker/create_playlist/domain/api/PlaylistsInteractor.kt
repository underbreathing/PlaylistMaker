package com.example.playlistmaker.create_playlist.domain.api

import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistsInteractor {

    suspend fun savePlaylist(playlist: Playlist)

    fun getPlayLists(): Flow<List<PlaylistInfo>>
    suspend fun addTrackToPlaylist(playlistInfo: PlaylistInfo, track: Track)

}