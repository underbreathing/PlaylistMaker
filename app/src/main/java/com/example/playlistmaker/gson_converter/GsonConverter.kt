package com.example.playlistmaker.gson_converter

import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import org.koin.core.component.KoinComponent

class GsonConverter(private val gson: Gson) : KoinComponent {

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

    fun trackIdsToJson(trackIds: List<Long>): String {
        return gson.toJson(trackIds)
    }

    fun jsonToListLong(json: String?): List<Long> {
        return if (json == null) emptyList()
        else gson.fromJson(json, Array<Long>::class.java).toList()
    }
}