package com.example.playlistmaker.playlist.ui.model

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

data class PlaylistCompleteDataUi(
    val generalInfo: PlaylistUi?,
    val totalTimeMinutes: String,
    val tracks: List<Track>
) {

    fun toString(context: Context): String {
        return generalInfo?.let { genInfo ->
            "${genInfo.title} ${if (genInfo.description.isNotBlank()) "\n${genInfo.description}" else ""} " +
                    "\n${
                        context.getString(
                            R.string.playlist_track_count,
                            genInfo.trackCount.toString(),
                            context.resources.getQuantityString(
                                R.plurals.track,
                                genInfo.trackCount
                            )
                        )
                    }" +

                    tracks.mapIndexed { index, track ->
                        "\n${index + 1}. ${track.artistName} - ${track.trackName} (${
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(track.trackTimeMillis)
                        }) "
                    }.joinToString()

        }.orEmpty()
    }
}