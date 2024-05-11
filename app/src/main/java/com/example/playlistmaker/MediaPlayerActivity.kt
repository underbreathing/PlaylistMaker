package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.search_tracks.Track
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_INTENT_DATA = "track_intent_data"

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var playerState = State.DEFAULT
    private lateinit var playButton: ImageButton
    private lateinit var trackCurrentTime: TextView
    private lateinit var uiHandler: Handler
    private lateinit var updatePlayProgressTask: Runnable

    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private companion object {
        const val TRACK_CURRENT_TIME_UPDATE_DELAY = 300L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        uiHandler = Handler(Looper.getMainLooper())

        val trackData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_INTENT_DATA, Track::class.java) as Track
        } else {
            intent.getParcelableExtra<Track>(TRACK_INTENT_DATA) as Track
        }

        playButton = findViewById(R.id.buttonPlay)
        playButton.isEnabled = false
        playButton.setOnClickListener {
            playButtonControl()
        }
        mediaPlayer = MediaPlayer()
        preparePlayer(trackData.previewUrl)


        val trackImage = findViewById<ImageView>(R.id.trackArtwork)
        Glide.with(this)
            .load(trackData?.getCoverArtwork())
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .into(trackImage)

        findViewById<TextView>(R.id.trackName).text = trackData?.trackName
        findViewById<TextView>(R.id.nameOfTheGroup).text = trackData?.artistName
        findViewById<TextView>(R.id.duration).text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackData?.trackTimeMillis)
        findViewById<TextView>(R.id.buttonTopBack).setOnClickListener { finish() }

        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val albumInfo = findViewById<TextView>(R.id.album)
        val albumIngoText = trackData?.collectionName
        if (albumIngoText != null) {
            albumGroup.isVisible = true
            albumInfo.text = albumIngoText
        }

        findViewById<TextView>(R.id.genre).text = trackData?.primaryGenreName
        findViewById<TextView>(R.id.country).text = trackData?.country
        val releaseDate = trackData.releaseDate ?: ""
        if (releaseDate.length > 3) {
            findViewById<TextView>(R.id.year).text = releaseDate.substring(0, 4)
        }
        trackCurrentTime = findViewById(R.id.trackCurrentTime)
        updatePlayProgressTask = object : Runnable {
            override fun run() {
                trackCurrentTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                uiHandler.postDelayed(this, TRACK_CURRENT_TIME_UPDATE_DELAY)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        uiHandler.removeCallbacks(updatePlayProgressTask)
        mediaPlayer.release()
    }

    private fun playButtonControl() {
        when (playerState) {
            State.PREPARED, State.PAUSED -> startPlayer()
            State.PLAYING -> pausePlayer()
            State.DEFAULT -> {}
        }
    }

    private fun preparePlayer(dataSource: String) {
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = State.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            uiHandler.removeCallbacks(updatePlayProgressTask)
            trackCurrentTime.text = getString(R.string.mediaplayer_play_progress_start)
            playerState = State.PREPARED
            playButton.setImageResource(R.drawable.ic_button_play)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        uiHandler.post(updatePlayProgressTask)
        playerState = State.PLAYING
        playButton.setImageResource(R.drawable.ic_button_pause)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        uiHandler.removeCallbacks(updatePlayProgressTask)
        playerState = State.PAUSED
        playButton.setImageResource(R.drawable.ic_button_play)
    }
}