package com.example.playlistmaker.di

import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.create_playlist.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.search.domain.use_cases.SearchTrackUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        SearchTrackUseCase(get())
    }

    factory<PlaylistsInteractor> { PlaylistsInteractorImpl(get()) }
}