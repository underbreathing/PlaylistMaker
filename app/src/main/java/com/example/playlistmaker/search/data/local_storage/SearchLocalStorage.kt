package com.example.playlistmaker.search.data.local_storage

import com.example.playlistmaker.search.domain.model.Track

interface SearchLocalStorage {
    fun putTrack(track: Track)

    fun getTracks(): List<Track>

    fun removeTrack(track: Track)
    fun clearStorage()
}