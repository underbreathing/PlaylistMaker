package com.example.playlistmaker.create_playlist.ui.mappers

import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.ui.model.PlaylistInfoUi

class PlaylistMapper {

    fun map(playlist: Playlist?): PlaylistInfoUi? {
        return playlist?.let {
            with(playlist) {
                PlaylistInfoUi(id, title, description.orEmpty(), trackIds, trackCount, coverPath)
            }
        }
    }

    fun map(playlistInfoUi: PlaylistInfoUi): Playlist {
        return with(playlistInfoUi) {
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