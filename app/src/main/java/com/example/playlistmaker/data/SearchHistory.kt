package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

const val KEY_LAST_TRACK_IN_HISTORY = "the_key_of_the_last_track_from_the_history"
const val KEY_TRACKS_HISTORY = "key_tracks_history"

class SearchHistory(private val sharedPrefs: SharedPreferences) {
    fun addTrackToHistory(track: Track) {
        sharedPrefs.edit()
            .putString(KEY_LAST_TRACK_IN_HISTORY, Gson().toJson(track))
            .apply()
    }

    fun addTrackToHistory(tracksHistory: List<Track>) {
        sharedPrefs.edit()
            .putString(KEY_TRACKS_HISTORY, Gson().toJson(tracksHistory))
            .apply()
    }

    fun getTracksHistory(): List<Track> {
        val trackHistoryJSON = sharedPrefs.getString(KEY_TRACKS_HISTORY, null)
        if (trackHistoryJSON != null) {
            return Gson().fromJson(trackHistoryJSON, Array<Track>::class.java).toList()
        }
        return listOf()
    }

    fun getLastTrackFromHistory(): Track? {
        val lastTrackJSON = sharedPrefs.getString(KEY_LAST_TRACK_IN_HISTORY, null)
        if (lastTrackJSON != null) {
            return deserializeTrackFromJSON(lastTrackJSON)
        }
        return null
    }

    private fun deserializeTrackFromJSON(trackJSON: String): Track {
        return Gson().fromJson(trackJSON, Track::class.java)
    }

}