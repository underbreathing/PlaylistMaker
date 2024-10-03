package com.example.playlistmaker.create_playlist.domain


import com.example.playlistmaker.create_playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {

   suspend fun savePlaylist(playlist: Playlist)

   fun getPlayLists(): Flow<List<Playlist>>
}