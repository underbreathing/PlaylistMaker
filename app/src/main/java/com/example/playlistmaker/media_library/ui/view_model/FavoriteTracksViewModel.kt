package com.example.playlistmaker.media_library.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media_library.ui.view_model.state.MediaLibraryDataState
import com.example.playlistmaker.media_library.domain.db.MediaLibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val mediaLibraryRepository: MediaLibraryRepository,
    private val appContext: Context
) : ViewModel() {

    private val mediaLibraryDataState: MutableLiveData<MediaLibraryDataState> = MutableLiveData()
    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaLibraryRepository.getMediaLibrary().collect { data ->
                if (data.isEmpty()) {
                    mediaLibraryDataState.postValue(
                        MediaLibraryDataState.Empty(
                            appContext.getString(
                                R.string.media_favorite_empty
                            )
                        )
                    )
                } else {
                    mediaLibraryDataState.postValue(MediaLibraryDataState.Content(data))
                }
            }
        }

    }

    fun observeMediaLibraryData(): LiveData<MediaLibraryDataState> = mediaLibraryDataState
}