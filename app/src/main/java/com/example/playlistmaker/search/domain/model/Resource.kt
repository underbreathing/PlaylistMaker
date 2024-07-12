package com.example.playlistmaker.search.domain.model

sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    class Error<T> : Resource<T>
}