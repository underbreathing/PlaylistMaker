package com.example.playlistmaker.presentation.mapper


import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.model.TrackInfo

object TrackMapper {

    fun map(track: Track): TrackInfo {
        var releaseDate = track.releaseDate
        if (releaseDate.length > 3) {
            releaseDate = releaseDate.substring(0, 4)
        }
        return TrackInfo(
            trackName = track.trackName,
            nameOfTheBand = track.artistName,
            duration = TimeFormatter.formatTheTime(track.trackTimeMillis),
            albumInfo = track.collectionName,
            genre = track.primaryGenreName,
            country = track.country,
            releaseYear = releaseDate,
            coverArtwork = track.getCoverArtwork()
        )
    }

}