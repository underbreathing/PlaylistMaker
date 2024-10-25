package com.example.playlistmaker.media_library.data.impl

import com.example.playlistmaker.gson_converter.GsonConverter
import com.example.playlistmaker.media_library.data.db.TrackDatabase
import com.example.playlistmaker.media_library.data.mappers.PlaylistTrackEntityMapper
import com.example.playlistmaker.media_library.data.mappers.TrackEntityMapper
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MediaLibraryRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val trackEntityMapper: TrackEntityMapper
) : MediaLibraryRepository {



    override suspend fun putToMediaLibrary(track: Track, additionTime: Long) {
        trackDatabase.getTrackDao().insertTrack(trackEntityMapper.map(track, additionTime))
    }

    override fun getMediaLibrary(): Flow<List<Track>> {
        return trackDatabase.getTrackDao().getTracks().map {
            it.map(trackEntityMapper::map)
        }
    }

    override fun isTrackInMediaLibrary(trackId: Long): Flow<Boolean> {
        return flow {
            emit(trackDatabase.getTrackDao().getTrackById(trackId) != null)
        }
    }

    override suspend fun deleteFromMediaLibrary(trackId: Long) {
        trackDatabase.getTrackDao().deleteTrackById(trackId)
    }
}