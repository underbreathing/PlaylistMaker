package com.example.playlistmaker.create_playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.create_playlist.data.db.entity.PlaylistEntity


@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("UPDATE playlist_table SET trackIds = :trackIds WHERE id = :playlistId")
    suspend fun updatePlaylistTrackIds(playlistId: Long, trackIds: String)
}