package com.example.playlistmaker.playlist.ui.model

import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.search.domain.model.Track

data class PlaylistCompleteDataUi(
    val generalInfo: PlaylistUi?,
    val totalTimeMinutes: String,
    val tracks: List<Track>
)