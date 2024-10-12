package com.example.playlistmaker.create_playlist.ui.mappers

import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo

class PlaylistMapper {

    fun map(playlist: Playlist): PlaylistInfo {
        return with(playlist) {
            PlaylistInfo(id, title, description.orEmpty(), trackIds, trackCount, coverPath)
        }
    }

    fun map(playlistInfo: PlaylistInfo): Playlist {
        return with(playlistInfo) {
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