package com.example.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query(
        "SELECT * FROM track_table " +
                "ORDER BY timeOfAddition DESC"
    )
    fun getTracks(): Flow<List<TrackEntity>>


    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?

    @Query("DELETE FROM track_table where trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)
}