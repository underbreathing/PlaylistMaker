package com.example.playlistmaker.media_library.ui.view_model.state

import com.example.playlistmaker.search.domain.model.Track

sealed interface MediaLibraryDataState {

    data class Content(val tracks: List<Track>): MediaLibraryDataState

    data class Empty(val message: String): MediaLibraryDataState
}