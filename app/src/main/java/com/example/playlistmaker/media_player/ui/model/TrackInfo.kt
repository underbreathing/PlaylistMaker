package com.example.playlistmaker.media_player.ui.model

data class TrackInfo(
    val trackName: String,
    val nameOfTheBand: String,
    val duration: String,
    val albumInfo: String?,
    val genre: String,
    val country: String,
    val releaseYear: String,
    val coverArtwork: String,
)