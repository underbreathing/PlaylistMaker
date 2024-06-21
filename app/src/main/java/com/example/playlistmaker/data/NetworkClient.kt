package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.NetworkResponse

interface NetworkClient {

    fun searchTracks(request: String): NetworkResponse
}