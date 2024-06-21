package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.NetworkResponse
import com.example.playlistmaker.data.dto.SearchTracksResponse
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.NetworkRepository

class NetworkRepositoryImpl(private val networkClient: NetworkClient): NetworkRepository {
    override fun searchTracks(request: String): Resource<List<Track>> {
        val response: NetworkResponse = networkClient.searchTracks(request)

        return if(response is SearchTracksResponse){
            Resource.Success(response.results)
        }else{
            Resource.Error("Сетевая ошибка")
        }
    }
}