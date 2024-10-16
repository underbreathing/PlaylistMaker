package com.example.playlistmaker.create_playlist.domain.impl

import com.example.playlistmaker.create_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val mediaLibraryRepository: MediaLibraryRepository
) : PlaylistsInteractor {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }


    override fun getPlayLists(): Flow<List<Playlist>> {
        return playlistRepository.getPlayLists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.updatePlaylist(playlist)
        mediaLibraryRepository.insertPlaylistTrack(track)
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist {
        return playlistRepository.getPlaylist(playlistId)
    }

    override fun getPlaylistTracks(playlistTracksIds: List<Long>): Flow<List<Track>> {
        return playlistRepository.getPlaylistTracks(playlistTracksIds)
    }

    override suspend fun deleteTrackFromPlaylist(updatedPlaylist: Playlist, track: Track) {
        playlistRepository.updatePlaylist(updatedPlaylist)
        mediaLibraryRepository.deleteTrack(track)
        //TODO проверить что вьюмодел получит список обновленных треков благодаря методу     override fun getPlaylistTracks(playlistTracksIds: List<Long>): Flow<List<Track>> {
    }

}