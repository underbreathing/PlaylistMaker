package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.model.Consumer
import com.example.playlistmaker.search.domain.model.ConsumerData
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.search.domain.use_cases.SearchTrackUseCase

class SearchViewModel(
    application: Application,
    private val historyRepository: TracksHistoryRepository,
    private val searchTrackUseCase: SearchTrackUseCase
) : AndroidViewModel(application) {

    fun clearHistory() {
        historyRepository.clearTracks()
        historyStateLiveData.postValue(HistoryState.EmptyHistory)
    }

    fun getHistoryStateLiveData(): LiveData<HistoryState> = historyStateLiveData

    private val historyStateLiveData: MutableLiveData<HistoryState> = MutableLiveData()

    fun purTrackInHistory(track: Track) {
        val history = getTracksHistory()
        val isUnique: Boolean = !history.contains(track)
        val isHistoryOverloaded = history.size == MAX_HISTORY_SIZE
        if (isUnique) {
            if (isHistoryOverloaded) {
                historyStateLiveData.postValue(HistoryState.NewUniqueTrack(track, true))
                removeTrackFromRepository(history.last())
            } else {
                historyStateLiveData.postValue(HistoryState.NewUniqueTrack(track, false))
            }
        } else {
            historyStateLiveData.postValue(HistoryState.NewNotUniqueTrack(track))
            removeTrackFromRepository(track)
        }
        putTrackInRepository(track)
        Log.d("MYY", " track historySize == ${getTracksHistory().size}")
    }


    private fun putTrackInRepository(track: Track) {
        historyRepository.putTrack(track)
    }

    private fun removeTrackFromRepository(track: Track) {
        historyRepository.removeTrack(track)
    }

    fun getTracksHistory(): List<Track> {
        return historyRepository.getTracks()
    }

    private val searchTextStateLiveData: MutableLiveData<String> = MutableLiveData()
    fun getSearchTextLiveData(): LiveData<String> = searchTextStateLiveData
    fun onTextChanged(newSearchText: String) {
        if (newSearchText.isEmpty()) {
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        }
        searchTextStateLiveData.value = newSearchText
    }


    private val searchStateLiveData: MutableLiveData<SearchState> = MutableLiveData()

    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var lastSearchRequest: String? = null
    private val handler = Handler(Looper.getMainLooper())

    //функция, которую будет использовать кнопка "обновить" т.к при ее нажатии ждать 2с необязательно
    fun search(searchQuery: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        handler.post { searchTrack(searchQuery) }

    }

    fun searchDebounce(searchQuery: String) {
        if (searchQuery == lastSearchRequest && searchStateLiveData.value !is SearchState.Error) {
            return
        }
        lastSearchRequest = searchQuery

        //перед новым запросом удаляем старый
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchTrack(searchQuery) }
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        )
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        super.onCleared()
    }

    private fun searchTrack(searchQuery: String) {
        if (searchQuery.isNotEmpty()) {
            changeSearchState(SearchState.IsLoading)
            searchTrackUseCase.execute(searchQuery, object : Consumer {
                override fun consume(consumedData: ConsumerData<List<Track>>) {
                    when (consumedData) {
                        is ConsumerData.Data -> {
                            val receivedTracks = consumedData.data
                            if (receivedTracks.isEmpty()) {
                                changeSearchState(
                                    SearchState.Empty(
                                        getApplication<Application>().getString(
                                            R.string.search_not_found
                                        )
                                    )
                                )
                            } else {
                                changeSearchState(
                                    SearchState.Content(consumedData.data)
                                )
                            }
                        }

                        is ConsumerData.Error -> {
                            val application = getApplication<Application>()
                            changeSearchState(
                                SearchState.Error(
                                    application.getString(R.string.search_internet_problem),
                                    application.getString(R.string.search_internet_problem_additional),
                                )
                            )

                        }
                    }
                }
            })
        }
    }

    private fun changeSearchState(newState: SearchState) {
        searchStateLiveData.postValue(newState)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        const val MAX_HISTORY_SIZE = 10
        fun getSearchViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                SearchViewModel(
                    application,
                    Creator.provideTracksHistoryRepository(application.applicationContext),
                    Creator.provideSearchTrackUseCase(application.applicationContext)
                )
            }
        }
    }
}