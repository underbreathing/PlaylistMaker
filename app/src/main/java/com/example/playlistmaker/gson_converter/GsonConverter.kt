package com.example.playlistmaker.gson_converter

import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson

object GsonConverter {

    private val gson = Gson()

    fun trackToJson(track: Track): String {
        return gson.toJson(track)
    }

    fun jsonToTrack(trackJson: String): Track {
        return gson.fromJson(trackJson, Track::class.java)
    }

    fun jsonToTrackList(tracks: String?): List<Track> {
        return if (!tracks.isNullOrEmpty()) {
            gson.fromJson(tracks, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    fun trackListToGson(tracks: List<Track>): String {
        return gson.toJson(tracks)
    }
}