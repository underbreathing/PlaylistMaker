package com.example.playlistmaker.search.domain.use_cases

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow

class SearchTrackUseCase(private val repository: NetworkRepository) {

    fun execute(request: String): Flow<Resource<List<Track>>> = repository.searchTracks(request)
}