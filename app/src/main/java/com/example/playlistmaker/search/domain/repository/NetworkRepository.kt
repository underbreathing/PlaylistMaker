package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track

interface NetworkRepository {
    fun searchTracks(request: String): Resource<List<Track>>
}