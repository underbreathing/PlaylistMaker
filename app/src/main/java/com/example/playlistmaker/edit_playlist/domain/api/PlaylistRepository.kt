package com.example.playlistmaker.edit_playlist.domain.api


import com.example.playlistmaker.edit_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {

    suspend fun savePlaylist(playlist: Playlist): Long

    fun getPlayLists(): Flow<List<Playlist?>>

    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylistTracks(playlistId: Long): Flow<List<Track>>

    suspend fun insertPlaylistTrack(track: Track)

    suspend fun deletePlaylistTrack(track: Track)

    suspend fun removePlaylist(playlistId: Long)

    suspend fun removePlaylistTracks(playlistTracks: List<Track>)
}