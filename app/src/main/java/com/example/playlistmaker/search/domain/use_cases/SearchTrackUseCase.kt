package com.example.playlistmaker.search.domain.use_cases

import android.util.Log
import com.example.playlistmaker.search.domain.model.Consumer
import com.example.playlistmaker.search.domain.model.ConsumerData
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.repository.NetworkRepository
import java.util.concurrent.Executors

class SearchTrackUseCase(private val repository: NetworkRepository) {

    private val executor = Executors.newCachedThreadPool()
    fun execute(request: String, consumer: Consumer) {
        executor.execute {
            //здесь производится сетевой запрос, поэтому делаем в отдельном потоке колл бэк реализует консьюмер
            when (val resource = repository.searchTracks(request)) {
                is Resource.Success -> {
                    Log.d("MYY", "use case execute resource success")
                    consumer.consume(ConsumerData.Data(resource.data))
                    //проверить в какую ветку пойдет если список треков пуст
                }

                is Resource.Error -> {
                    Log.d("MYY", "use case execute resource error")
                    consumer.consume(ConsumerData.Error())
                }
            }
        }


    }
}