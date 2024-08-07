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

class NetworkRepositoryImpl(private val remoteDataSource: RemoteDataSource) : NetworkRepository {
    override fun searchTracks(request: String): Resource<List<Track>> {
        val response: NetworkResponse = remoteDataSource.doRequest(TrackSearchRequest(request))

        //здесь проверка только на тип ответа
        //коды не проверяются, т.к у нас один placeholder и на отсутствие интернета и на любые ошибки сервера
        return if (response is SearchTracksResponse) {
            Resource.Success(response.results.map(TrackDtoMapper::map))
        } else {
            Resource.Error()
        }
    }
}