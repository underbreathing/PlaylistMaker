package com.example.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media_library.data.db.dao.TrackDao
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class]
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
}