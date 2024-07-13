package com.example.playlistmaker.di

import com.example.playlistmaker.search.domain.use_cases.SearchTrackUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        SearchTrackUseCase(get())
    }
}