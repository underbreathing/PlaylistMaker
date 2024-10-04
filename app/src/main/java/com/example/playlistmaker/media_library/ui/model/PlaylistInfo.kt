package com.example.playlistmaker.media_library.ui.model

data class PlaylistInfo(
    val id: Long,
    val title: String,
    val trackCount: Int,
    val coverUriString: String?
)