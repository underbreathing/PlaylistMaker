package com.example.playlistmaker.data.remote_data_source

import android.content.Context
import com.example.playlistmaker.data.server_response.NetworkResponse

//
class RemoteDataSourceImpl(private val context: Context): RemoteDataSource {

    //Здесь задача только получить ответ, а не обработать его. Знает о типе ответа и обрабатывает его уже репозиторий
    override fun searchTracks(request: String): NetworkResponse {
        return try {
            val response = RetrofitClient.api.searchTrack(request).execute()
            val networkResponse: NetworkResponse = response.body() ?: NetworkResponse()

            networkResponse.resultCode = response.code()
            networkResponse
        }catch (ex: Exception){
            NetworkResponse().apply { resultCode = 400 }
        }
    }

    override fun getContext(): Context{
        return context
    }
}