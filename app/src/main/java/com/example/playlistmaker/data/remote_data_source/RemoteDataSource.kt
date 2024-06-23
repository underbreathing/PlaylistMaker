package com.example.playlistmaker.data.remote_data_source

import android.content.Context
import com.example.playlistmaker.data.server_response.NetworkResponse

interface RemoteDataSource {

    fun searchTracks(request: String): NetworkResponse

    fun getContext(): Context
}