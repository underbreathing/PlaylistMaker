package com.example.playlistmaker.create_playlist.domain.impl

import com.example.playlistmaker.create_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.create_playlist.ui.mappers.PlaylistMapper
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val mediaLibraryRepository: MediaLibraryRepository,
    private val playlistMapper: PlaylistMapper
) : PlaylistsInteractor {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }


    override fun getPlayLists(): Flow<List<PlaylistInfo>> {
        return playlistRepository.getPlayLists().map {
            it.map(playlistMapper::map)
        }
    }

    override suspend fun addTrackToPlaylist(playlistInfo: PlaylistInfo, track: Track) {
        playlistRepository.updatePlaylist(playlistMapper.map(playlistInfo))
        mediaLibraryRepository.insertPlaylistTrack(track)
    }
}