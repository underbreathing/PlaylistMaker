package com.example.playlistmaker.create_playlist.data.impl

import com.example.playlistmaker.create_playlist.data.mappers.PlaylistMapper
import com.example.playlistmaker.create_playlist.domain.PlaylistRepository
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.data.db.TrackDatabase

class PlaylistRepositoryImpl(
    private val playlistDb: TrackDatabase,
    private val playlistMapper: PlaylistMapper
) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistDb.getPlaylistDao().insertPlaylist(playlistMapper.map(playlist))
    }
}