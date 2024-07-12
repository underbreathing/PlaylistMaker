package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.dto.TrackDto

class SearchTracksResponse(val resultCount: Int, val results: List<TrackDto>): NetworkResponse()