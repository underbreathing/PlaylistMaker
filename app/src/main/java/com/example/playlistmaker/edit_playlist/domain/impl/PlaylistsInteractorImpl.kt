package com.example.playlistmaker.edit_playlist.domain.impl

import com.example.playlistmaker.edit_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.edit_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.edit_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistRepository: PlaylistRepository,
) : PlaylistsInteractor {

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }


    override fun getPlayLists(): Flow<List<Playlist?>> {
        return playlistRepository.getPlayLists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.updatePlaylist(playlist)
        playlistRepository.insertPlaylistTrack(track)
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylist(playlistId)
    }

    override fun getPlaylistTracks(playlistId: Long): Flow<List<Track>> {
        return playlistRepository.getPlaylistTracks(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(updatedPlaylist: Playlist, track: Track) {
        playlistRepository.updatePlaylist(updatedPlaylist)
        playlistRepository.deletePlaylistTrack(track)
    }

    override suspend fun deletePlaylist(playlistId: Long, playlistTracks: List<Track>) {
        playlistRepository.removePlaylist(playlistId)
        playlistRepository.removePlaylistTracks(playlistTracks)
    }

}