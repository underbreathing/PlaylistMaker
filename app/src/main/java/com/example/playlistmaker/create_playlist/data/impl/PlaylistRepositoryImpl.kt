package com.example.playlistmaker.create_playlist.data.impl

import com.example.playlistmaker.create_playlist.data.mappers.PlaylistEntityMapper
import com.example.playlistmaker.create_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.data.db.TrackDatabase
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDb: TrackDatabase,
    private val playlistEntityMapper: PlaylistEntityMapper
) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistDb.getPlaylistDao().insertPlaylist(playlistEntityMapper.map(playlist))
    }

    override fun getPlayLists(): Flow<List<Playlist>> {
        return playlistDb.getPlaylistDao().getPlaylists().map {
            it.map(playlistEntityMapper::map)
        }
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist? {
        return playlistDb.getPlaylistDao().getPlaylistById(playlistId)
            ?.let(playlistEntityMapper::map)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDb.getPlaylistDao().updatePlaylist(playlistEntityMapper.map(playlist))
    }
}