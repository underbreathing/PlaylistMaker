package com.example.playlistmaker.search.data.remote_data_source

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ITunesApi by lazy {
        client.create(ITunesApi::class.java)
    }


}