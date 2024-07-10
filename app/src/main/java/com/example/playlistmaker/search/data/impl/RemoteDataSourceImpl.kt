package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.remote_data_source.RemoteDataSource
import com.example.playlistmaker.search.data.remote_data_source.RetrofitClient
import com.example.playlistmaker.search.data.dto.NetworkResponse

//
class RemoteDataSourceImpl(private val context: Context) : RemoteDataSource {

    //Здесь задача только получить ответ, а не обработать его. Знает о типе ответа и обрабатывает его уже репозиторий
    override fun doRequest(dto: Any): NetworkResponse {
        return if (!isConnected()) {
            NetworkResponse().apply { resultCode = -1 }
        } else {
            if (dto is TrackSearchRequest) {
                val response = RetrofitClient.api.searchTrack(dto.request).execute()
                val networkResponse: NetworkResponse = response.body() ?: NetworkResponse()
                networkResponse.apply { resultCode = response.code() }

            } else {
                NetworkResponse().apply { resultCode = 400 }
            }
        }
    }


    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            }
        }
        return false
    }


    override fun getContext(): Context {
        return context
    }
}