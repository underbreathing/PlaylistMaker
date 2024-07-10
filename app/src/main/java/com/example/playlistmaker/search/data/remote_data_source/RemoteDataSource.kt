package com.example.playlistmaker.search.data.remote_data_source

import android.content.Context
import com.example.playlistmaker.search.data.dto.NetworkResponse

interface RemoteDataSource {

    fun doRequest(dto: Any): NetworkResponse

    fun getContext(): Context
}