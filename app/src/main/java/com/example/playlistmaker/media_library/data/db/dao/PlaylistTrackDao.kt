package com.example.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistTrackDao {

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table")
    fun getAllPlaylistTracks(): Flow<List<PlaylistTrackEntity>>

    @Delete(entity = PlaylistTrackEntity::class)
    suspend fun deletePlaylistTrack(playlistTrackEntity: PlaylistTrackEntity)
}