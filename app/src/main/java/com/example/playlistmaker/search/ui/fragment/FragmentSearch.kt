package com.example.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.media_player.ui.fragment.FragmentMediaPlayer
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.view_model.HistoryState
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.GlobalScope
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentSearch : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true

    //history
    private var tracksHistory: ArrayList<Track> = arrayListOf()
    private lateinit var trackAdapterHistory: TrackAdapter

    //...

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        binding.searchInputLine.requestFocus()

        viewModel.observeSearchStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is SearchState.Content -> showContent(it.tracks)
                is SearchState.Empty -> showEmpty(it.message)
                is SearchState.Error -> showError(it.errorMessage, it.additionalMessage)
                SearchState.IsLoading -> showLoading()
            }
        }
        trackAdapter = TrackAdapter {
            trackDebounce(it)
        }

        viewModel.getHistoryStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                HistoryState.EmptyHistory -> {
                    showEmptyHistory()
                }

                is HistoryState.NewNotUniqueTrack -> {
                    showAddNewNotUniqueTrack(it)
                }

                is HistoryState.NewUniqueTrack -> {
                    showAddNewUniqueTrack(it)
                }

                is HistoryState.InitState -> {
                    showInitState(it.history)
                }
            }
        }

        val searchTracksRecycler = binding.searchTracksRecycler
        searchTracksRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchTracksRecycler.adapter = trackAdapter


        val inputLine = binding.searchInputLine
        val clearButton = binding.clearIcon
        clearButton.setOnClickListener {
            inputLine.setText("")
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputLine.windowToken, 0)
            clearTrackList()
        }

        binding.bUpdate.setOnClickListener {
            viewModel.search(inputLine.text.toString())
        }

        viewModel.observeSearchTextLiveData().observe(viewLifecycleOwner) { newSearchText ->
            clearButton.isVisible = newSearchText.isNotEmpty()
            binding.layoutHistory.isVisible =
                newSearchText.isEmpty() && inputLine.hasFocus() && tracksHistory.isNotEmpty()
            if (newSearchText.isEmpty()) {
                clearTrackList()
            } else {
                viewModel.searchDebounce(newSearchText)
            }
        }

        inputLine.addTextChangedListener(onTextChanged = { charSequence, _, _, _ ->
            viewModel.onTextChanged(charSequence.toString())
        })


        val recyclerHistory = binding.searchTracksHistoryRecycler

        viewModel.initHistory()
        trackAdapterHistory = TrackAdapter {
            trackDebounce(it)
        }
        recyclerHistory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerHistory.adapter = trackAdapterHistory

        inputLine.setOnFocusChangeListener { _, hasFocus ->
            binding.layoutHistory.isVisible =
                hasFocus && inputLine.text.isEmpty() && tracksHistory.isNotEmpty()
        }

        val buttonClearHistory = binding.buttonClearHistory
        buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

    }

    private fun showInitState(history: List<Track>) {
        tracksHistory = ArrayList(history)
        trackAdapterHistory.setNewItems(history)
        trackAdapterHistory.notifyDataSetChanged()
    }

    private fun trackDebounce(track: Track) {
        if (isClickAllowed) {
            viewModel.purTrackInHistory(track)
            isClickAllowed = false
            debounce<Boolean>(
                false, GlobalScope,
                TrackAdapter.CLICK_DEBOUNCE_DELAY
            ) { value ->
                isClickAllowed = value
            }.invoke(true)
            findNavController().navigate(
                R.id.action_fragmentSearch_to_fragmentMediaPlayer,
                FragmentMediaPlayer.createArgs(track)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddNewUniqueTrack(it: HistoryState.NewUniqueTrack) {
        if (it.historyOverloaded) {
            val lastIndex = tracksHistory.lastIndex
            tracksHistory.removeLast()
            trackAdapterHistory.setNewItems(tracksHistory)
            trackAdapterHistory.notifyItemRemoved(lastIndex)
        }
        showPutTrackOnTopHistory(it.track)
    }

    private fun showAddNewNotUniqueTrack(it: HistoryState.NewNotUniqueTrack) {
        val currentTrack = it.track
        val indexOfCopy = tracksHistory.indexOf(currentTrack)
        tracksHistory.remove(currentTrack)
        trackAdapterHistory.setNewItems(tracksHistory)
        trackAdapterHistory.notifyItemRemoved(indexOfCopy)
        trackAdapterHistory.notifyItemRangeChanged(indexOfCopy, tracksHistory.size)
        showPutTrackOnTopHistory(currentTrack)
    }

    private fun showEmptyHistory() {
        tracksHistory.clear()
        trackAdapterHistory.setNewItems(tracksHistory)
        trackAdapterHistory.notifyDataSetChanged()
        binding.layoutHistory.isVisible = false
    }

    private fun showPutTrackOnTopHistory(track: Track) {
        tracksHistory.add(0, track)
        trackAdapterHistory.setNewItems(tracksHistory)
        trackAdapterHistory.notifyItemInserted(0)
        trackAdapterHistory.notifyItemRangeChanged(0, tracksHistory.size)
    }

    private fun clearTrackList() {
        trackAdapter.setNewItems(emptyList())
        trackAdapter.notifyDataSetChanged()
        clearPlaceholdersVisibility()
    }


    private fun showLoading() {
        clearPlaceholdersVisibility()
        binding.searchProgressBar.isVisible = true
    }

    private fun showError(message: String, additionalMessage: String = "") {
        binding.searchProgressBar.isVisible = false
        showMessage(message, additionalMessage, R.drawable.no_internet, true)
    }

    private fun showEmpty(message: String) {
        binding.searchProgressBar.isVisible = false
        clearPlaceholdersVisibility()
        showMessage(message, "", R.drawable.tracks_not_found)
    }

    private fun showContent(content: List<Track>) {
        Log.d("MYY", "showContent invoked")
        binding.searchProgressBar.isVisible = false
        clearPlaceholdersVisibility()
        trackAdapter.setNewItems(content)
        trackAdapter.notifyDataSetChanged()
    }

    private fun clearPlaceholdersVisibility() {
        binding.problemTitle.isVisible = false
        binding.problemImage.isVisible = false
        binding.problemAdditionalMessage.isVisible = false
        binding.bUpdate.isVisible = false
        binding.layoutPlaceholders.isVisible = false
    }

    private fun setTitlesVisibility(titlesVisibility: Boolean) {
        binding.problemTitle.isVisible = titlesVisibility
        binding.problemImage.isVisible = titlesVisibility
    }

    private fun showMessage(
        title: String,
        additional: String,
        imageId: Int,
        internetProblem: Boolean = false
    ) {
        if (title.isNotEmpty()) {
            binding.layoutPlaceholders.isVisible = true
            trackAdapter.setNewItems(emptyList())
            trackAdapter.notifyDataSetChanged()
            binding.problemTitle.text = title
            binding.problemImage.setImageResource(imageId)
            setTitlesVisibility(true)
            if (additional.isNotEmpty()) {
                binding.problemAdditionalMessage.text = additional
                binding.problemAdditionalMessage.isVisible = true
            }
            if (internetProblem) {
                binding.bUpdate.isVisible = true
            }
        }
    }
}