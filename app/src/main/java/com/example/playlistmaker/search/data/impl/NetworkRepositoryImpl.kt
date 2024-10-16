package com.example.playlistmaker.search.data.impl


import androidx.core.content.ContextCompat.getString
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.dto.SearchTracksResponse
import com.example.playlistmaker.search.data.mapper.TrackDtoMapper
import com.example.playlistmaker.search.data.remote_data_source.RemoteDataSource
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(private val remoteDataSource: RemoteDataSource, private val trackDtoMapper: TrackDtoMapper) : NetworkRepository {
    override fun searchTracks(request: String): Flow<Resource<List<Track>>> = flow {
        val response: NetworkResponse = remoteDataSource.doRequest(TrackSearchRequest(request))

        if (response is SearchTracksResponse) {
            emit(Resource.Success(response.results.map(trackDtoMapper::map)))
        } else {
            emit(Resource.Error())
        }
    }
}