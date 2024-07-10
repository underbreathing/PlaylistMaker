package com.example.playlistmaker.search.domain.model

interface Consumer {

    fun consume(consumerData: ConsumerData<List<Track>>)
}