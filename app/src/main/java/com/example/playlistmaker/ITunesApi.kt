package com.example.playlistmaker

import com.example.playlistmaker.data.dto.SearchTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<SearchTrackResponse>
}