package com.example.playlistmaker.edit_playlist.domain.api

import com.example.playlistmaker.edit_playlist.domain.model.Playlist

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistsInteractor {

    suspend fun savePlaylist(playlist: Playlist)

    fun getPlayLists(): Flow<List<Playlist?>>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getPlaylistTracks(playlistId: Long): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(updatedPlaylist: Playlist, track: Track)

    suspend fun deletePlaylist(playlistId: Long, playlistTracks: List<Track>)
}