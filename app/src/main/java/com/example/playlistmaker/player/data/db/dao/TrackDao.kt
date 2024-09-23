package com.example.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.player.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query(
        "SELECT * FROM track_table " +
                "ORDER BY timeOfAddition DESC"
    )
    suspend fun getTracks(): List<TrackEntity>

    //возможно выбросит NullPointerException посмотрим на это. Fix: TrackEntity?
    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?

    @Query("DELETE FROM track_table where trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)
}