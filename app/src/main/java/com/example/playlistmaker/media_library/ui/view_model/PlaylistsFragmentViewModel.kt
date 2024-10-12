package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.create_playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.media_library.ui.view_model.state.PlaylistsDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {

    private val stateLiveData: MutableLiveData<PlaylistsDataState> = MutableLiveData()

    init {
        fillData()
    }

    fun observeDataState(): LiveData<PlaylistsDataState> {
        return stateLiveData
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlayLists().collect { data ->
                if (data.isEmpty()) {
                    stateLiveData.postValue(PlaylistsDataState.Empty)
                } else {
                    stateLiveData.postValue(PlaylistsDataState.Content(data))
                }

            }
        }

    }

}