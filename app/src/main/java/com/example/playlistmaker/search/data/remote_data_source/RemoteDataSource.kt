package com.example.playlistmaker.search.data.remote_data_source

import com.example.playlistmaker.search.data.dto.NetworkResponse

interface RemoteDataSource {
    suspend fun doRequest(dto: Any): NetworkResponse

}