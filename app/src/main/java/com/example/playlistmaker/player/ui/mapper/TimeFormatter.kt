package com.example.playlistmaker.player.ui.mapper

import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_TIME_FORMAT = "mm:ss"

object TimeFormatter {

    fun formatTheTime(time: Long): String {
        return SimpleDateFormat(
            TRACK_TIME_FORMAT,
            Locale.getDefault()
        ).format(time)
    }
}