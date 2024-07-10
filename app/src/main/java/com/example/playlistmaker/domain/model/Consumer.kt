package com.example.playlistmaker.domain.model

interface Consumer {

    fun consume(consumerData: ConsumerData<List<Track>>)
}