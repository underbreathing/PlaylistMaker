package com.example.playlistmaker.domain.use_cases_and_interactors

import com.example.playlistmaker.domain.model.Consumer
import com.example.playlistmaker.domain.model.ConsumerData
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.repository.NetworkRepository
import java.util.concurrent.Executors

class SearchTrackUseCase(private val repository: NetworkRepository) {

    private val executor = Executors.newCachedThreadPool()
    fun execute(request: String, consumer: Consumer) {
        executor.execute {
            //здесь производится сетевой запрос, поэтому делаем в отдельном потоке колл бэк реализует консьюмер
            when (val resource = repository.searchTracks(request)) {
                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(resource.data))
                }

                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(resource.message,resource.additionalMessage))
                }
            }
        }


    }
}