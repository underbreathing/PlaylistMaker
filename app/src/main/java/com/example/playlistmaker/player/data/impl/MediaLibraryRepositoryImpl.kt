package com.example.playlistmaker.player.data.impl

import com.example.playlistmaker.player.data.db.TrackDatabase
import com.example.playlistmaker.player.data.mappers.TrackEntityMapper
import com.example.playlistmaker.player.domain.db.MediaLibraryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaLibraryRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val trackEntityMapper: TrackEntityMapper
) : MediaLibraryRepository {
    override suspend fun putToMediaLibrary(track: Track, additionTime: Long) {
        trackDatabase.getTrackDao().insertTrack(trackEntityMapper.map(track, additionTime))
    }

    override suspend fun getMediaLibrary(): Flow<List<Track>> = flow {
        emit(trackDatabase.getTrackDao().getTracks().map(trackEntityMapper::map))
    }

    override suspend fun isTrackInMediaLibrary(trackId: Long): Flow<Boolean> = flow {
        emit(trackDatabase.getTrackDao().getTrackById(trackId) != null)
    }

    override suspend fun deleteFromMediaLibrary(trackId: Long) {
        trackDatabase.getTrackDao().deleteTrackById(trackId)
    }

}