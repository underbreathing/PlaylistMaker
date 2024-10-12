package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.domain.model.Track

//sealed class HistoryState(val data: List<Track>) {
//
//    class EmptyHistory() : HistoryState(emptyList())
//    class History(data: List<Track>) : HistoryState(data)
//
//    class HistoryWithRemoved(
//        data: List<Track>,
//        val removedTrackIndex: Int,
//        val removedIsLast: Boolean
//    ) : HistoryState(data)
//    //index показывать какой notify нам нужно сделать
//
//}

sealed interface HistoryState {

    data class InitState(val history: List<Track>) : HistoryState

    data object EmptyHistory : HistoryState

    data class NewUniqueTrack(val track: Track, val historyOverloaded: Boolean) : HistoryState

    data class NewNotUniqueTrack(val track: Track) : HistoryState
}