package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track

class TrackDtoMapper {

    fun map(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl = trackDto.artworkUrl,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
    }
}