package com.example.playlistmaker.media_library.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlaylistUi(
    val id: Long,
    val title: String,
    val description: String,
    val trackIds: List<Long>,
    val trackCount: Int,
    val coverUriString: String?
) : Parcelable