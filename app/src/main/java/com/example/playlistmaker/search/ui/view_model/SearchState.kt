package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.domain.model.Track

sealed interface SearchState {

    data object IsLoading : SearchState

    data class Content(val tracks: List<Track>): SearchState

    data class Error(val errorMessage: String, val additionalMessage: String = ""): SearchState

    data class Empty(val message: String): SearchState
}