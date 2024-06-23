package com.example.playlistmaker.data.repository


import androidx.core.content.ContextCompat.getString
import com.example.playlistmaker.R
import com.example.playlistmaker.data.server_response.NetworkResponse
import com.example.playlistmaker.data.server_response.SearchTracksResponse
import com.example.playlistmaker.data.mapper.TrackDtoMapper
import com.example.playlistmaker.data.remote_data_source.RemoteDataSource
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.NetworkRepository

class NetworkRepositoryImpl(private val remoteDataSource: RemoteDataSource) : NetworkRepository {
    override fun searchTracks(request: String): Resource<List<Track>> {
        val response: NetworkResponse = remoteDataSource.searchTracks(request)

        return if (response is SearchTracksResponse) {
            Resource.Success(response.results.map { TrackDtoMapper.map(it) })
        } else {
            val context = remoteDataSource.getContext()
            Resource.Error(
                getString(context, R.string.search_internet_problem),
                getString(context, R.string.search_internet_problem_additional)
            )
        }
    }
}