package com.example.playlistmaker.create_playlist.domain.api

import com.example.playlistmaker.create_playlist.domain.model.Playlist

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistsInteractor {

    suspend fun savePlaylist(playlist: Playlist)

    fun getPlayLists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    suspend fun getPlaylist(playlistId: Long): Playlist

    fun getPlaylistTracks(playlistTracksIds: List<Long>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(updatedPlaylist: Playlist, track: Track)
}