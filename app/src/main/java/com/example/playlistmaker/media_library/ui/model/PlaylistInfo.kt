package com.example.playlistmaker.media_library.ui.model

data class PlaylistInfo(
    val id: Long,
    val title: String,
    val description: String,
    val trackIds: List<Long>,
    val trackCount: Int,
    val coverUriString: String?
)