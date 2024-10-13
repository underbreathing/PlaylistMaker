package com.example.playlistmaker.media_library.data.mappers

import com.example.playlistmaker.media_library.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.search.domain.model.Track

class PlaylistTrackEntityMapper {

    fun map(track: Track): PlaylistTrackEntity {
        return with(track) {
            PlaylistTrackEntity(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
                System.currentTimeMillis()
            )
        }
    }

    fun map(playlistTrackEntity: PlaylistTrackEntity): Track {
        return with(playlistTrackEntity) {
            Track(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }
}