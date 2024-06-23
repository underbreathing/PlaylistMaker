package com.example.playlistmaker.data.server_response

import com.example.playlistmaker.data.dto.TrackDto

class SearchTracksResponse(val resultCount: Int, val results: List<TrackDto>): NetworkResponse()