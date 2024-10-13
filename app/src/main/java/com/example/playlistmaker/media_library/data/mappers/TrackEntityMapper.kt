package com.example.playlistmaker.media_library.data.mappers

import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.model.Track

class TrackEntityMapper {

    fun map(track: Track, additionTime: Long): TrackEntity {
        return with(track) {
            TrackEntity(
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
                additionTime
            )
        }
    }

    fun map(trackEntity: TrackEntity): Track {
        return with(trackEntity) {
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