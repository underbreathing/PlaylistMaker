package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.debounce
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.search.domain.use_cases.SearchTrackUseCase
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val historyRepository: TracksHistoryRepository,
    private val searchTrackUseCase: SearchTrackUseCase
) : AndroidViewModel(application) {

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val MAX_HISTORY_SIZE = 10
    }

    private val searchTextStateLiveData: MutableLiveData<String> = MutableLiveData()
    private val historyStateLiveData: MutableLiveData<HistoryState> = MutableLiveData()
    private val searchStateLiveData: MutableLiveData<SearchState> = MutableLiveData()
    private var lastSearchRequest: String? = null
    private var _searchDebounce: (String) -> Unit = debounce(
        true, viewModelScope,
        SEARCH_DEBOUNCE_DELAY
    ) { searchText: String -> searchTrack(searchText) }


    fun clearHistory() {
        historyRepository.clearTracks()
        historyStateLiveData.postValue(HistoryState.EmptyHistory)
    }

    fun getHistoryStateLiveData(): LiveData<HistoryState> = historyStateLiveData

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

    fun observeSearchTextLiveData(): LiveData<String> = searchTextStateLiveData
    fun onTextChanged(newSearchText: String) {
        searchTextStateLiveData.value = newSearchText
    }

    fun observeSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    //функция, которую будет использовать кнопка "обновить" т.к при ее нажатии ждать 2с необязательно
    fun search(searchQuery: String) {
        searchTrack(searchQuery)
    }

    fun initHistory() {
        historyStateLiveData.value = HistoryState.InitState(getTracksHistory())
    }


    fun searchDebounce(searchQuery: String) {
        if (searchQuery == lastSearchRequest && searchStateLiveData.value !is SearchState.Error) {
            return
        }
        lastSearchRequest = searchQuery

        _searchDebounce(searchQuery)

    }

    private fun searchTrack(searchQuery: String) {
        if (searchQuery.isNotEmpty()) {
            renderSearchState(SearchState.IsLoading)

            viewModelScope.launch {
                searchTrackUseCase(searchQuery).collect { receivedData ->
                    when (receivedData) {
                        is Resource.Success -> {
                            processSuccess(receivedData)
                        }

                        is Resource.Error -> {
                            processError()
                        }
                    }
                }
            }
        }
    }

    private fun processError() {
        val application = getApplication<Application>()
        renderSearchState(
            SearchState.Error(
                application.getString(R.string.search_internet_problem),
                application.getString(R.string.search_internet_problem_additional),
            )
        )
    }

    private fun processSuccess(receivedData: Resource.Success<List<Track>>) {
        val receivedTracks = receivedData.data
        if (receivedTracks.isEmpty()) {
            renderSearchState(
                SearchState.Empty(
                    getApplication<Application>().getString(
                        R.string.search_not_found
                    )
                )
            )
        } else {
            renderSearchState(
                SearchState.Content(receivedData.data)
            )
        }
    }

    private fun renderSearchState(newState: SearchState) {
        searchStateLiveData.postValue(newState)
    }

    private fun putTrackInRepository(track: Track) {
        historyRepository.putTrack(track)
    }

    private fun getTracksHistory(): List<Track> {
        return historyRepository.getTracks()
    }

    private fun removeTrackFromRepository(track: Track) {
        historyRepository.removeTrack(track)
    }
}