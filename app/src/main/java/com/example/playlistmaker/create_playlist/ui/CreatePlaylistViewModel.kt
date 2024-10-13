package com.example.playlistmaker.create_playlist.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.api.LocalFileStorage
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.create_playlist.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreatePlaylistViewModel(
    private val localFileStorage: LocalFileStorage,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _isSavingCompletedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isSavingCompleted: LiveData<Boolean> = _isSavingCompletedLiveData


    fun savePlaylist(title: String, description: String?, coverUri: Uri?) {

        viewModelScope.launch(Dispatchers.IO) {

            val savedFileUri: String? = coverUri?.let { localFileStorage.saveImage(it, title) }
            Log.d("MYY", "half time")

            playlistsInteractor.savePlaylist(
                Playlist(
                    0L,
                    title,
                    description,
                    savedFileUri,
                    emptyList(),
                    0
                )
            )

            _isSavingCompletedLiveData.postValue(true)
        }

    }

}