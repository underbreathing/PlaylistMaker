package com.example.playlistmaker.create_playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.create_playlist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("UPDATE playlist_table SET trackIds = :trackIds WHERE id = :playlistId")
    suspend fun updatePlaylistTrackIds(playlistId: Long, trackIds: String)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Update(PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
}