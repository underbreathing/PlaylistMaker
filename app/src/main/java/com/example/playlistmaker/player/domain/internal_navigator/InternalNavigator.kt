package com.example.playlistmaker.player.domain.internal_navigator

import com.example.playlistmaker.search.domain.model.Track

interface InternalNavigator {

    fun getArrivedTrack(): Track?
}