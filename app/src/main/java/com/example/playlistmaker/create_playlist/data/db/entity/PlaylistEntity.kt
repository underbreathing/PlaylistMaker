package com.example.playlistmaker.create_playlist.data.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    val title: String,
    val description: String?,
    val coverUri: String?,
    val trackIds: String?,
    val trackCount: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)