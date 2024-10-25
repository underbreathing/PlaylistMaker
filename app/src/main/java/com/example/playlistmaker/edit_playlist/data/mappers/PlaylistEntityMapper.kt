package com.example.playlistmaker.edit_playlist.data.mappers

import com.example.playlistmaker.edit_playlist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.edit_playlist.domain.model.Playlist
import com.example.playlistmaker.gson_converter.GsonConverter

class PlaylistEntityMapper(private val gsonConverter: GsonConverter) {

    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                title,
                description,
                coverPath,
                gsonConverter.trackIdsToJson(trackIds),
                trackCount,
                id
            )
        }
    }

    fun map(playlistEntity: PlaylistEntity?): Playlist? {
        return playlistEntity?.let {
            with(playlistEntity) {
                Playlist(
                    id,
                    title,
                    description,
                    coverUri,
                    gsonConverter.jsonToListLong(trackIds),
                    trackCount
                )
            }
        }
    }
}