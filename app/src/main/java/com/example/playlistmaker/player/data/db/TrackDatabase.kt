package com.example.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.player.data.db.dao.TrackDao
import com.example.playlistmaker.player.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class]
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
}