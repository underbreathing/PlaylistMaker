package com.example.playlistmaker.playlist.ui.view_model.model

import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.search.domain.model.Track

sealed interface PlaylistScreenData {

    data class GeneralInfo(val generalInfo: PlaylistUi?): PlaylistScreenData

    data class Tracks(val tracks: List<Track>, val totalTimeMinutes: String, val tracksCount: Int): PlaylistScreenData
}