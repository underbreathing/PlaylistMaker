package com.example.playlistmaker.gson_converter

import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson

object GsonConverter {

    fun trackToJson(track: Track): String {
        return Gson().toJson(track)
    }

    fun jsonToTrack(trackJson: String): Track {
        return Gson().fromJson(trackJson, Track::class.java)
    }

    fun jsonToTrackList(tracks: String?): List<Track> {
        return if (!tracks.isNullOrEmpty()) {
            Gson().fromJson(tracks, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    fun trackListToGson(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }
}