package com.example.playlistmaker.playlist.ui

import com.example.playlistmaker.media_library.ui.model.PlaylistInfoUi
import com.example.playlistmaker.search.domain.model.Track

data class PlaylistCompleteDataUi(
    val generalInfo: PlaylistInfoUi?,
    val totalTimeMinutes: String,
    val tracks: List<Track>
)