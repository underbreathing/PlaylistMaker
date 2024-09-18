package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.remote_data_source.RemoteDataSource
import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.remote_data_source.ITunesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//
class RemoteDataSourceImpl(private val context: Context, private val iTunesService: ITunesApi) :
    RemoteDataSource {

    //Здесь задача только получить ответ, а не обработать его. Знает о типе ответа и обрабатывает его уже репозиторий
    override suspend fun doRequest(dto: Any): NetworkResponse {
        return if (!isConnected()) {
            NetworkResponse().apply { resultCode = -1 }
        } else {
            withContext(Dispatchers.IO) {
                try {
                    if (dto is TrackSearchRequest) {
                        iTunesService.searchTrack(dto.request).apply { resultCode = 200 }
                    } else {
                        NetworkResponse().apply { resultCode = 400 }
                    }
                } catch (e: Throwable) {
                    NetworkResponse().apply { resultCode = 500 }
                }
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
}