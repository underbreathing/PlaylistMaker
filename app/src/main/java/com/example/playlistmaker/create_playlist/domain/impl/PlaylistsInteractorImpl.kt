package com.example.playlistmaker.create_playlist.domain.impl

import com.example.playlistmaker.create_playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.create_playlist.domain.mappers.PlaylistMapper
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.gson_converter.GsonConverter
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistMapper: PlaylistMapper,
    private val gsonConverter: GsonConverter
) : PlaylistsInteractor {
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }


    override fun getPlayLists(): Flow<List<PlaylistInfo>> {
        return playlistRepository.getPlayLists().map {
            it.map(playlistMapper::map)
        }
    }

    override suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long): Boolean {
        val playlist: Playlist? = playlistRepository.getPlaylist(playlistId)
        if (playlist == null) {
            return false
        } else {
            val trackIds = playlist.trackIds
            return if (trackIds != null) {
                val trackIdList: MutableList<Long> =
                    gsonConverter.jsonToListLong(trackIds).toMutableList()
                if (trackIdList.contains(trackId)) {
                    false
                } else {
                    trackIdList.add(trackId)
                    putNewPlaylistInRep(playlist, trackIdList)
                    true
                }
            } else {
                putNewPlaylistInRep(playlist, listOf(trackId))
                true
            }
        }
    }

    private suspend fun putNewPlaylistInRep(
        playlist: Playlist,
        trackIdList: List<Long>
    ) {
        playlistRepository.updatePlaylist(with(playlist) {
            Playlist(
                id,
                title,
                description,
                coverPath,
                gsonConverter.trackIdsToJson(trackIdList),
                trackCount + 1
            )
        })
    }
}