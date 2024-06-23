package com.example.playlistmaker.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    @SerializedName("artworkUrl100")
    val artworkUrl: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)
