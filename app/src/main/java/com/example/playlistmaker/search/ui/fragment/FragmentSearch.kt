package com.example.playlistmaker.search.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.activity.MediaPlayerActivity
import com.example.playlistmaker.player.ui.activity.TRACK_INTENT_DATA
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.view_model.HistoryState
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSearch : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter

    //history
    private lateinit var tracksHistory: ArrayList<Track>
    private lateinit var trackAdapterHistory: TrackAdapter

    //...
    private val uiHandler: Handler = Handler(Looper.getMainLooper())

    private var isClickOnTrackAllowed = true
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

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is SearchState.Content -> showContent(it.tracks)
                is SearchState.Empty -> showEmpty(it.message)
                is SearchState.Error -> showError(it.errorMessage, it.additionalMessage)
                SearchState.IsLoading -> showLoading()
            }
        }
        trackAdapter = TrackAdapter(tracks) {
            onClickTrack(it)
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

        binding.buttonUpdate.setOnClickListener {
            viewModel.search(inputLine.text.toString())
        }

        viewModel.getSearchTextLiveData().observe(viewLifecycleOwner) { newSearchText ->
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


        tracksHistory = ArrayList(viewModel.getTracksHistory())
        trackAdapterHistory = TrackAdapter(tracksHistory)
        {
            onClickTrack(it)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddNewUniqueTrack(it: HistoryState.NewUniqueTrack) {
        if (it.historyOverloaded) {
            val lastIndex = tracksHistory.lastIndex
            tracksHistory.removeLast()
            trackAdapterHistory.notifyItemRemoved(lastIndex)
        }
        showPutTrackOnTopHistory(it.track)
    }

    private fun showAddNewNotUniqueTrack(it: HistoryState.NewNotUniqueTrack) {
        val currentTrack = it.track
        val indexOfCopy = tracksHistory.indexOf(currentTrack)
        tracksHistory.remove(currentTrack)
        trackAdapterHistory.notifyItemRemoved(indexOfCopy)
        trackAdapterHistory.notifyItemRangeChanged(indexOfCopy, tracksHistory.size)
        showPutTrackOnTopHistory(currentTrack)
    }

    private fun showEmptyHistory() {
        tracksHistory.clear()
        trackAdapterHistory.notifyDataSetChanged()
        //inputLine.clearFocus() // можно и так. чтобы вызвался обработчик изменения состояния фокуса и убрал пустую историю из видимости
        binding.layoutHistory.isVisible = false
    }

    private fun showPutTrackOnTopHistory(track: Track) {
        tracksHistory.add(0, track)
        trackAdapterHistory.notifyItemInserted(0)
        trackAdapterHistory.notifyItemRangeChanged(0, tracksHistory.size)
    }

    private fun onClickTrack(currentTrack: Track) {
        if (clickDebounce()) {
            viewModel.purTrackInHistory(currentTrack)
            val intent = Intent(context, MediaPlayerActivity::class.java)

            intent.putExtra(TRACK_INTENT_DATA, currentTrack)
            startActivity(intent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickOnTrackAllowed
        if (isClickOnTrackAllowed) {
            isClickOnTrackAllowed = false
            uiHandler.postDelayed({ isClickOnTrackAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clearTrackList() {
        tracks.clear()
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
        tracks.clear()
        showMessage(message, "", R.drawable.tracks_not_found)
    }

    private fun showContent(content: List<Track>) {
        Log.d("MYY", "showContent invoked")
        binding.searchProgressBar.isVisible = false
        clearPlaceholdersVisibility()
        tracks.clear()
        tracks.addAll(content)
        trackAdapter.notifyDataSetChanged()
    }

    private fun clearPlaceholdersVisibility() {
        binding.problemTitle.isVisible = false
        binding.problemImage.isVisible = false
        binding.problemAdditionalMessage.isVisible = false
        binding.buttonUpdate.isVisible = false
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
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            binding.problemTitle.text = title
            binding.problemImage.setImageResource(imageId)
            setTitlesVisibility(true)
            if (additional.isNotEmpty()) {
                binding.problemAdditionalMessage.text = additional
                binding.problemAdditionalMessage.isVisible = true
            }
            if (internetProblem) {
                binding.buttonUpdate.isVisible = true
            }
        }
    }

    private companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}