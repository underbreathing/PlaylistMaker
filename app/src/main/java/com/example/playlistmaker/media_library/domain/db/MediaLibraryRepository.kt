package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryRepository {

    suspend fun putToMediaLibrary(track: Track, additionTime: Long)

    suspend fun getMediaLibrary(): Flow<List<Track>>

    suspend fun isTrackInMediaLibrary(trackId: Long): Flow<Boolean>

    suspend fun deleteFromMediaLibrary(trackId: Long)

}