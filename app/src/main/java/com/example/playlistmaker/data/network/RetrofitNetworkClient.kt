package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.NetworkResponse

class RetrofitNetworkClient: NetworkClient {

    //Здесь задача только получить ответ, а не обработать его. Знает о типе ответа и обрабатывает его уже репозиторий
    override fun searchTracks(request: String): NetworkResponse  {
        return try {
            val response = RetrofitClient.api.searchTrack(request).execute()
            val networkResponse: NetworkResponse = response.body() ?: NetworkResponse()

            networkResponse.resultCode = response.code()
            networkResponse
        }catch (ex: Exception){
            NetworkResponse().apply { resultCode = 400 }
        }
    }
}