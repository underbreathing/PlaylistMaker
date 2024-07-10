package com.example.playlistmaker.player.data.impl

import android.app.Activity
import android.os.Build
import com.example.playlistmaker.player.domain.internal_navigator.InternalNavigator
import com.example.playlistmaker.player.ui.activity.TRACK_INTENT_DATA
import com.example.playlistmaker.search.domain.model.Track

class InternalNavigatorImpl(val activity: Activity) : InternalNavigator {
    override fun getArrivedTrack(): Track? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.intent.getParcelableExtra(TRACK_INTENT_DATA, Track::class.java)
        } else {
            activity.intent.getParcelableExtra(TRACK_INTENT_DATA)
        }
    }
}