package com.example.playlistmaker.create_playlist.data.mappers

import com.example.playlistmaker.create_playlist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.create_playlist.domain.model.Playlist

class PlaylistMapper {

    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                title,
                description,
                coverPath,
                trackIds,
                trackCount
            )
        }
    }
}