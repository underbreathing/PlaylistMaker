package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

object TrackDtoMapper {

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