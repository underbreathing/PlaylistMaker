package com.example.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.gson_converter.GsonConverter
import com.example.playlistmaker.search.data.local_storage.SearchLocalStorage
import com.example.playlistmaker.search.domain.model.Track

class SearchLocalStorageImpl(private val sharedPrefs: SharedPreferences) : SearchLocalStorage {

    companion object {
        const val TRACKS_HISTORY_KEY = "tracks_history_key"
    }

    override fun getTracks(): List<Track> {
        return GsonConverter.jsonToTrackList(sharedPrefs.getString(TRACKS_HISTORY_KEY, null))
    }

    override fun clearStorage() {
        sharedPrefs.edit().clear().apply()
    }

    private fun changeLocalStorage(track: Track, remove: Boolean) {
        val tracksHistoryMutable = getTracks().toMutableList()
        var modified = true
        if (remove) {
            if (!tracksHistoryMutable.remove(track)) {
                modified = false
            }
        } else {
            tracksHistoryMutable.add(0, track)
        }
        if (modified) {
            sharedPrefs.edit()
                .putString(TRACKS_HISTORY_KEY, GsonConverter.trackListToGson(tracksHistoryMutable))
                .apply()
        }
    }


    override fun putTrack(track: Track) {
        changeLocalStorage(track, false)
    }

    override fun removeTrack(track: Track) {
        changeLocalStorage(track, true)
    }

}