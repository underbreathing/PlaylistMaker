package com.example.playlistmaker.create_playlist.domain.mappers

import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo

class PlaylistMapper {

    fun map(playlist: Playlist): PlaylistInfo{
        return with(playlist){
            PlaylistInfo(id,title,trackCount,coverPath)
        }
    }
}