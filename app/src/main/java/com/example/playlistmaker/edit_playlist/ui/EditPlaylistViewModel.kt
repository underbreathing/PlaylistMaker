package com.example.playlistmaker.edit_playlist.ui

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.edit_playlist.domain.api.LocalFileStorage
import com.example.playlistmaker.edit_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.edit_playlist.ui.mappers.PlaylistMapper
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    localFileStorage: LocalFileStorage,
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistMapper: PlaylistMapper
) : CreatePlaylistViewModel(localFileStorage, playlistsInteractor) {

    fun updatePlaylist(
        oldPlaylist: PlaylistUi?,
        title: String,
        description: String,
        uri: Uri?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var isNeedToUpdate = false
            oldPlaylist?.let { playlist ->
                if (playlist.coverUriString != uri.toString()) {
                    saveImage(uri, playlist.id)
                    isNeedToUpdate = true
                }
                if (playlist.title != title || playlist.description != description) {
                    isNeedToUpdate = true
                }
                if (isNeedToUpdate) {
                    playlistsInteractor.updatePlaylist(
                        playlistMapper.map(
                            oldPlaylist.copy(
                                title = title,
                                description = description,
                                coverUriString = uri.toString()
                            )
                        )
                    )
                }
                isSavingCompletedLiveData.postValue(true)
            }
        }

    }


}