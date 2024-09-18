package com.example.playlistmaker.search.data.remote_data_source

import com.example.playlistmaker.search.data.dto.SearchTracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): SearchTracksResponse
}