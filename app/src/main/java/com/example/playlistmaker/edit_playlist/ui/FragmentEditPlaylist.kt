package com.example.playlistmaker.edit_playlist.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentEditPlaylist : FragmentCreatePlaylist() {

    companion object {
        private const val PLAYLIST_INFO_KEY = "playlist info key"

        fun createArgs(playlistInfo: PlaylistUi): Bundle {
            return bundleOf(PLAYLIST_INFO_KEY to playlistInfo)
        }
    }

    override val viewModel: EditPlaylistViewModel by viewModel()
}