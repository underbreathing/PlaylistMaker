package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search_tracks.Track
import com.google.gson.Gson

const val KEY_LAST_TRACK_IN_HISTORY = "the_key_of_the_last_track_from_the_history"
const val KEY_TRACKS_HISTORY = "key_tracks_history"
class SearchHistory(private val sharedPrefs: SharedPreferences) {
    fun addTrackToHistory(track: Track) {
        sharedPrefs.edit()
            .putString(KEY_LAST_TRACK_IN_HISTORY,Gson().toJson(track))
            .apply()
    }

    fun saveTracksHistory(tracksHistory: ArrayList<Track>){
        sharedPrefs.edit()
            .putString(KEY_TRACKS_HISTORY,Gson().toJson(tracksHistory))
            .apply()
    }

    fun getTracksHistory():ArrayList<Track>{
        val trackHistoryJSON = sharedPrefs.getString(KEY_TRACKS_HISTORY,null)
        if(trackHistoryJSON != null){
            return ArrayList(Gson().fromJson(trackHistoryJSON,Array<Track>::class.java).toList())
        }
        return arrayListOf()
    }

}