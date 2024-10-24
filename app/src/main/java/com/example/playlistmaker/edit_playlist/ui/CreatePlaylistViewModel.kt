package com.example.playlistmaker.edit_playlist.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.edit_playlist.domain.api.LocalFileStorage
import com.example.playlistmaker.edit_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.edit_playlist.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class CreatePlaylistViewModel(
    private val localFileStorage: LocalFileStorage,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    protected open val isSavingCompletedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isSavingCompleted: LiveData<Boolean> = isSavingCompletedLiveData


    fun savePlaylist(title: String, description: String?, coverUri: Uri?) {

        viewModelScope.launch(Dispatchers.IO) {

            saveImage(coverUri)
            Log.d("MYY", "half time")

            playlistsInteractor.savePlaylist(
                Playlist(
                    0L,
                    title,
                    description,
                    coverUri.toString(),
                    emptyList(),
                    0
                )
            )

            isSavingCompletedLiveData.postValue(true)
        }

    }

    protected open suspend fun saveImage(coverUri: Uri?) {
        coverUri?.let {
            localFileStorage.saveImage(it)
        }
    }

}