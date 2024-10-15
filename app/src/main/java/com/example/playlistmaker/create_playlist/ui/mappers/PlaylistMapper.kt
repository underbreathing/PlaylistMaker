package com.example.playlistmaker.create_playlist.ui.mappers

import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.ui.model.PlaylistUi

class PlaylistMapper {

    fun map(playlist: Playlist?): PlaylistUi? {
        return playlist?.let {
            with(playlist) {
                PlaylistUi(id, title, description.orEmpty(), trackIds, trackCount, coverPath)
            }
        }
    }

    fun map(playlistUi: PlaylistUi): Playlist {
        return with(playlistUi) {
            Playlist(
                id,
                title,
                description,
                coverUriString,
                trackIds,
                trackCount
            )
        }
    }
}