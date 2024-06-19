package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.dto.SearchTrackResponse
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.MediaPlayerActivity
import com.example.playlistmaker.ui.TRACK_INTENT_DATA
import com.example.playlistmaker.ui.track.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val KEY_TRACKS_HISTORY_FILE = "the_key_to_the_track_history_file"
const val MAX_HISTORY_SIZE = 10

class SearchActivity : AppCompatActivity() {


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private var inputLineText: String = ""
    private lateinit var titleProblem: TextView
    private lateinit var additionalMessage: TextView
    private lateinit var buttonUpdate: Button
    private lateinit var placeholder: ImageView
    private lateinit var layoutPlaceholders: LinearLayout
    private lateinit var inputLine: EditText
    private lateinit var tracksHistory: ArrayList<Track>
    private lateinit var trackAdapterHistory: TrackAdapter
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var sharedHistoryListener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var searchHistory: SearchHistory
    private val uiHandler: Handler = Handler(Looper.getMainLooper())
    private val searchTask = Runnable { searchTrack() }
    private lateinit var searchProgressBar: ProgressBar
    private var isClickOnTrackAllowed =
        true // одна переменная для треков из результата запроса и для треков из истории


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        sharedPrefs = getSharedPreferences(KEY_TRACKS_HISTORY_FILE, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        trackAdapter = TrackAdapter(tracks) {
            onClickTrack(it)
        }
        sharedHistoryListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_LAST_TRACK_IN_HISTORY) {
                val addedTrack = searchHistory.getLastTrackFromHistory()
                if (addedTrack != null) {
                    val indexOfCopy =
                        tracksHistory.indexOfFirst { e -> e.trackId == addedTrack.trackId }
                    if (indexOfCopy != -1) {
                        tracksHistory.removeAt(indexOfCopy)
                        trackAdapterHistory.notifyItemRemoved(indexOfCopy)
                        trackAdapterHistory.notifyItemRangeChanged(indexOfCopy, tracksHistory.size)
                    } else if (tracksHistory.size == MAX_HISTORY_SIZE) {
                        tracksHistory.removeLast()
                        trackAdapterHistory.notifyItemRemoved(tracksHistory.lastIndex)
                    }
                    tracksHistory.add(0, addedTrack)
                    trackAdapterHistory.notifyItemInserted(0)
                    trackAdapterHistory.notifyItemRangeChanged(0, tracksHistory.size)
                }

            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedHistoryListener)
        val layoutHistory =
            findViewById<LinearLayout>(R.id.layout_history) //чтобы управлять видимостью всех элементов которые находятся в нем
        val tracksRecyclerView = findViewById<RecyclerView>(R.id.search_tracks)
        tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = trackAdapter

        val buttonBack = findViewById<TextView>(R.id.search_button_back)
        buttonBack.setOnClickListener {
            finish()
        }
        inputLine = findViewById(R.id.search_input_line)
        inputLine.setText(inputLineText)

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputLine.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputLine.windowToken, 0)
            clearTrackList()
        }

        buttonUpdate = findViewById(R.id.button_update)
        titleProblem = findViewById(R.id.problem_title)
        additionalMessage = findViewById(R.id.problem_additional_message)
        placeholder = findViewById(R.id.problem_image)
        layoutPlaceholders = findViewById(R.id.layout_placeholders)
        searchProgressBar = findViewById(R.id.search_progress_bar)


        buttonUpdate.setOnClickListener {
            searchTrack()
        }


        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = setButtonVisibility(s)
                inputLineText = s.toString()
                layoutHistory.isVisible =
                    s?.isEmpty() == true && inputLine.hasFocus() && tracksHistory.isNotEmpty()
                if (s?.isNotEmpty() == true) {
                    searchDebounce()
                } else {
                    uiHandler.removeCallbacks(searchTask)
                    clearTrackList()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        inputLine.addTextChangedListener(searchTextWatcher)


        val recyclerHistory = findViewById<RecyclerView>(R.id.history_of_tracks)

        tracksHistory = ArrayList(searchHistory.getTracksHistory())
        trackAdapterHistory = TrackAdapter(tracksHistory) {
            onClickTrack(it)
        }
        recyclerHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerHistory.adapter = trackAdapterHistory

        inputLine.setOnFocusChangeListener { _, hasFocus ->
            layoutHistory.isVisible =
                hasFocus && inputLine.text.isEmpty() && tracksHistory.isNotEmpty()
        }
        val buttonClearHistory = findViewById<Button>(R.id.button_clear_history)
        buttonClearHistory.setOnClickListener {
            tracksHistory.clear()
            trackAdapterHistory.notifyDataSetChanged()
            //inputLine.clearFocus() // можно и так. чтобы вызвался обработчик изменения состояния фокуса и убрал пустую историю из видимости
            layoutHistory.isVisible = false
        }
    }

    private fun onClickTrack(currentTrack: Track){
        if (clickDebounce()) {
            searchHistory.addTrackToHistory(currentTrack)
            val intent = Intent(this, MediaPlayerActivity::class.java)

            intent.putExtra(TRACK_INTENT_DATA, currentTrack)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        uiHandler.removeCallbacks(searchTask)
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedHistoryListener)
        super.onDestroy()
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

    override fun onStop() {
        super.onStop()
        searchHistory.saveTracksHistory(tracksHistory)
    }

    private fun searchDebounce() {
        uiHandler.removeCallbacks(searchTask)
        uiHandler.postDelayed(searchTask, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchTrack() {
        clearPlaceholdersVisibility()
        searchProgressBar.isVisible = true
        iTunesService.searchTrack(inputLine.text.toString())
            .enqueue(object : Callback<SearchTrackResponse> {
                override fun onResponse(
                    call: Call<SearchTrackResponse>,
                    response: Response<SearchTrackResponse>
                ) {
                    searchProgressBar.isVisible = false
                    if (response.code() == 200) {
                        clearPlaceholdersVisibility()
                        tracks.clear()
                        if (response.body()?.resultCount!! > 0) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        } else {
                            showMessage(
                                getString(R.string.search_not_found),
                                "",
                                R.drawable.tracks_not_found
                            )
                        }
                    } else {
                        showMessage(
                            getString(R.string.search_internet_problem),
                            getString(R.string.search_internet_problem_additional),
                            R.drawable.no_internet,
                            true
                        )
                    }
                }

                override fun onFailure(call: Call<SearchTrackResponse>, t: Throwable) {
                    searchProgressBar.isVisible = false
                    showMessage(
                        getString(R.string.search_internet_problem),
                        getString(R.string.search_internet_problem_additional),
                        R.drawable.no_internet,
                        true
                    )

                }

            })
    }

    private fun clearPlaceholdersVisibility() {
        titleProblem.isVisible = false
        placeholder.isVisible = false
        additionalMessage.isVisible = false
        buttonUpdate.isVisible = false
        layoutPlaceholders.isVisible = false
    }

    private fun addPlaceholdersVisibility(
        titlesVisibility: Boolean = false,
        additionalVisibility: Boolean = false,
        buttonUpdateVisibility: Boolean = false
    ) {
        if (titlesVisibility) {
            titleProblem.isVisible = true
            placeholder.isVisible = true
        }
        if (additionalVisibility) {
            additionalMessage.isVisible = true
        }
        if (buttonUpdateVisibility) {
            buttonUpdate.isVisible = true
        }
    }

    private fun showMessage(
        title: String,
        additional: String,
        imageId: Int,
        internetProblem: Boolean = false
    ) {
        if (title.isNotEmpty()) {
            layoutPlaceholders.isVisible = true
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            titleProblem.text = title
            placeholder.setImageResource(imageId)
            addPlaceholdersVisibility(titlesVisibility = true)
            if (additional.isNotEmpty()) {
                additionalMessage.text = additional
                addPlaceholdersVisibility(additionalVisibility = true)
            }
            if (internetProblem) {
                addPlaceholdersVisibility(buttonUpdateVisibility = true)
            }
        }
    }

    private fun setButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_LINE_TEXT, inputLineText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputLineText = savedInstanceState.getString(INPUT_LINE_TEXT) ?: ""
    }


    private companion object {
        const val INPUT_LINE_TEXT = "INPUT_LINE_TEXT"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}