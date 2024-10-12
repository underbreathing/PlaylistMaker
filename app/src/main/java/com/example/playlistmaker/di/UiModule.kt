package com.example.playlistmaker.di

import com.example.playlistmaker.create_playlist.ui.mappers.PlaylistMapper
import com.example.playlistmaker.media_player.ui.animations.PlayerAnimations
import com.example.playlistmaker.media_player.ui.mapper.TrackMapper
import org.koin.dsl.module

val uiModule = module {

    factory {
        PlayerAnimations()
    }

    single { PlaylistMapper() }
    single { TrackMapper() }
}