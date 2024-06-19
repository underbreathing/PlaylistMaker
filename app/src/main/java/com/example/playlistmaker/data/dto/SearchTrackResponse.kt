package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.entity.Track

class SearchTrackResponse(val resultCount: Int,val results: List<Track>)