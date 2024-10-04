package com.example.playlistmaker.media_library.ui.view_model.state

import com.example.playlistmaker.media_library.ui.model.PlaylistInfo

sealed interface PlaylistsDataState {

    data object Empty : PlaylistsDataState
    data class Content(val data: List<PlaylistInfo>) : PlaylistsDataState
}