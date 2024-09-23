package com.example.playlistmaker.player.ui.mapper


import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.player.ui.model.TrackInfo

class TrackMapper {

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