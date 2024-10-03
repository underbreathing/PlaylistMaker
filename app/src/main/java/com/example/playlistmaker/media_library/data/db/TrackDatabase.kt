package com.example.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.create_playlist.data.db.dao.PlaylistDao
import com.example.playlistmaker.create_playlist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.data.db.dao.TrackDao
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class]
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao

    abstract fun getPlaylistDao(): PlaylistDao
}