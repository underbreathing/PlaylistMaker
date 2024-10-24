package com.example.playlistmaker.edit_playlist.data.db.entity

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