package com.example.playlistmaker.di

import com.example.playlistmaker.player.ui.animations.PlayerAnimations
import org.koin.dsl.module

val uiModule = module {

    factory {
        PlayerAnimations()
    }
}