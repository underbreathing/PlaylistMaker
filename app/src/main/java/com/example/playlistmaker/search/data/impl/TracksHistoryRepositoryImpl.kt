package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.local_storage.SearchLocalStorage
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TracksHistoryRepository

class TracksHistoryRepositoryImpl(private val searchLocalStorage: SearchLocalStorage) :
    TracksHistoryRepository {
    override fun putTrack(track: Track) {
        searchLocalStorage.putTrack(track)
    }

    override fun getTracks(): List<Track> {
        return searchLocalStorage.getTracks()
    }

    override fun removeTrack(track: Track) {
        searchLocalStorage.removeTrack(track)
    }

    override fun clearTracks() {
        searchLocalStorage.clearStorage()
    }


}