package com.example.playlistmaker.search.domain.model

sealed interface ConsumerData<T> {
    data class Data<T>(val data: T): ConsumerData<T>
    class Error<T>: ConsumerData<T>
}