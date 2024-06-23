package com.example.playlistmaker.data.remote_data_source

import com.example.playlistmaker.data.server_response.SearchTracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<SearchTracksResponse>
}