package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track

interface TracksHistoryRepository {
    fun putTrack(track: Track)
    fun getTracks(): List<Track>
    fun removeTrack(track: Track)

    fun clearTracks()
}