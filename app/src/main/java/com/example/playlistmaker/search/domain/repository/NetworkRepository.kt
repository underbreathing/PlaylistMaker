package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun searchTracks(request: String): Flow<Resource<List<Track>>>
}