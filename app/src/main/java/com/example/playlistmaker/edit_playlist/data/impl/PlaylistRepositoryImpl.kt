package com.example.playlistmaker.edit_playlist.data.impl

import com.example.playlistmaker.edit_playlist.data.mappers.PlaylistEntityMapper
import com.example.playlistmaker.edit_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.edit_playlist.domain.model.Playlist
import com.example.playlistmaker.gson_converter.GsonConverter
import com.example.playlistmaker.media_library.data.db.TrackDatabase
import com.example.playlistmaker.media_library.data.mappers.PlaylistTrackEntityMapper
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDb: TrackDatabase,
    private val playlistEntityMapper: PlaylistEntityMapper,
    private val playlistTrackEntityMapper: PlaylistTrackEntityMapper,
    private val gsonConverter: GsonConverter
) : PlaylistRepository {

    override suspend fun insertPlaylistTrack(track: Track) {
        playlistDb.getPlaylistTrackDao()
            .insertTrack(playlistTrackEntityMapper.map(track))
    }

    override suspend fun deletePlaylistTrack(track: Track) {
        if (!isContainedInAnyPlaylist(track.trackId)) {
            playlistDb.getPlaylistTrackDao()
                .deletePlaylistTrack(playlistTrackEntityMapper.map(track))
        }
    }

    override suspend fun removePlaylist(playlistId: Long) {
        playlistDb.getPlaylistDao().deletePlaylist(playlistId)
    }

    override suspend fun removePlaylistTracks(playlistTracks: List<Track>) {
        playlistTracks.forEach { track ->
            deletePlaylistTrack(track)
        }
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        playlistDb.getPlaylistDao().insertPlaylist(playlistEntityMapper.map(playlist))
    }

    override fun getPlayLists(): Flow<List<Playlist?>> {
        return playlistDb.getPlaylistDao().getPlaylistsFlow().map {
            it.map(playlistEntityMapper::map)
        }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistDb.getPlaylistDao().getPlaylistFlowById(playlistId)
            .map(playlistEntityMapper::map)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDb.getPlaylistDao().updatePlaylist(playlistEntityMapper.map(playlist))
    }

    override fun getPlaylistTracks(playlistId: Long): Flow<List<Track>> {
        return flow {
            getPlaylist(playlistId).collect { playlist ->
                emit(playlistDb.getPlaylistTrackDao().getAllPlaylistTracks().map {
                    playlistTrackEntityMapper.map(it)
                }.filter { track ->
                    playlist?.trackIds?.contains(track.trackId) ?: false
                })

            }
        }
    }

    private suspend fun isContainedInAnyPlaylist(trackId: Long): Boolean {
        return playlistDb.getPlaylistDao().getPlaylists().map { it.trackIds }
            .flatMap(gsonConverter::jsonToListLong).contains(trackId)
    }
}