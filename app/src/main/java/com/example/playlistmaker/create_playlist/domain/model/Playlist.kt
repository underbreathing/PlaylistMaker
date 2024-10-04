package com.example.playlistmaker.create_playlist.domain.model

data class Playlist(
    val id: Long,
    val title: String,
    val description: String?,
    val coverPath: String?,
    val trackIds: String?,
    val trackCount: Int
)