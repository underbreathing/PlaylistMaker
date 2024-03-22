package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.search_tracks.ITunesApi
import com.example.playlistmaker.search_tracks.SearchTrackResponse
import com.example.playlistmaker.search_tracks.Track
import com.example.playlistmaker.search_tracks.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)
    private var inputLineText: String = ""
    private lateinit var titleProblem: TextView
    private lateinit var additionalMessage: TextView
    private lateinit var buttonUpdate: Button
    private lateinit var placeholder: ImageView
    private lateinit var inputLine: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val tracksRecyclerView = findViewById<RecyclerView>(R.id.search_tracks)
        tracksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = trackAdapter

        val buttonBack = findViewById<TextView>(R.id.search_button_back)
        buttonBack.setOnClickListener {
            finish()
        }
        inputLine = findViewById<EditText>(R.id.search_input_line)
        inputLine.setText(inputLineText)

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputLine.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputLine.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
        }

        buttonUpdate = findViewById(R.id.button_update)
        titleProblem = findViewById(R.id.problem_title)
        additionalMessage = findViewById(R.id.problem_additional_message)
        placeholder = findViewById(R.id.problem_image)

        buttonUpdate.setOnClickListener {
            searchTrack()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = clearButtonVisibility(s)
                inputLineText = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        inputLine.addTextChangedListener(searchTextWatcher)


        inputLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }
    }

    private fun searchTrack(){
        iTunesService.searchTrack(inputLine.text.toString()).enqueue(object : Callback<SearchTrackResponse>{
            override fun onResponse(
                call: Call<SearchTrackResponse>,
                response: Response<SearchTrackResponse>
            ) {
                if(response.code() == 200){
                    clearPlaceholdersVisibility()
                    tracks.clear()
                    if(response.body()?.resultCount!! > 0){
                        tracks.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    }else{
                        showMessage(getString(R.string.search_not_found),"",R.drawable.tracks_not_found)
                    }
                }else{
                    showMessage(getString(R.string.search_internet_problem),getString(R.string.search_internet_problem_additional),
                        R.drawable.no_internet,true)
                }
            }

            override fun onFailure(call: Call<SearchTrackResponse>, t: Throwable) {
                showMessage(getString(R.string.search_internet_problem),getString(R.string.search_internet_problem_additional),
                    R.drawable.no_internet,true)

            }

        })
    }
    private fun clearPlaceholdersVisibility(){
        titleProblem.isVisible = false
        placeholder.isVisible = false
        additionalMessage.isVisible = false
        buttonUpdate.isVisible = false
    }
    private fun addPlaceholdersVisibility(
        titlesVisibility: Boolean = false,
        additionalVisibility: Boolean = false,
        buttonUpdateVisibility: Boolean = false
    ) {
        if(titlesVisibility) {
            titleProblem.isVisible = true
            placeholder.isVisible = true
        }
        if(additionalVisibility) {
            additionalMessage.isVisible = true
        }
        if(buttonUpdateVisibility){
            buttonUpdate.isVisible = true
        }
    }

    private fun showMessage(
        title: String ,
        additional: String ,
        imageId: Int ,
        internetProblem: Boolean = false
    ) {
        if (title.isNotEmpty()) {
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

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
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
    }

}