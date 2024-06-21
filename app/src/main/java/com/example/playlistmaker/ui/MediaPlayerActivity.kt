package com.example.playlistmaker.ui


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
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.AudioInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.mapper.TimeFormatter
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.model.TrackInfo

const val TRACK_INTENT_DATA = "track_intent_data"

class MediaPlayerActivity : AppCompatActivity() {

    private val audioInteractor: AudioInteractor = Creator.provideAudioInteractor()

    private lateinit var playButton: ImageButton
    private lateinit var trackCurrentTime: TextView
    private val uiHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var updatePlayProgressTask: Runnable

    private companion object {
        const val TRACK_CURRENT_TIME_UPDATE_DELAY = 300L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val trackData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_INTENT_DATA, Track::class.java)
        } else {
            intent.getParcelableExtra(TRACK_INTENT_DATA)
        }

        val trackInfo = trackData?.let { TrackMapper.map(it) }
        trackInfo?.let { showTrackInfo(it) }

        findViewById<TextView>(R.id.buttonTopBack).setOnClickListener { finish() }
        playButton = findViewById(R.id.buttonPlay)
        playButton.isEnabled = false
        playButton.setOnClickListener {
            audioInteractor.playControl({
                uiHandler.post(updatePlayProgressTask)
                playButton.setImageResource(R.drawable.ic_button_pause)
            }, {
                audioPlayerOnPause()
            })
        }
        trackData?.let {
            audioInteractor.preparePlayer(it.previewUrl, {
                playButton.isEnabled = true
            }, {
                uiHandler.removeCallbacks(updatePlayProgressTask)
                trackCurrentTime.text = getString(R.string.mediaplayer_play_progress_start)
                playButton.setImageResource(R.drawable.ic_button_play)
            })
        }


        trackCurrentTime = findViewById(R.id.trackCurrentTime)

        //задача обновления времени плеера
        updatePlayProgressTask = object : Runnable {
            override fun run() {
                trackCurrentTime.text =
                    TimeFormatter.formatTheTime(audioInteractor.currentPosition().toLong())
                uiHandler.postDelayed(this, TRACK_CURRENT_TIME_UPDATE_DELAY)
            }
        }

    }

    private fun showTrackInfo(trackInfo: TrackInfo) {

        val trackImage = findViewById<ImageView>(R.id.trackArtwork)
        //Не придумал как вынести использование Glide
        //чат гпт говорит, что можно использовать здесь (в presentation) напрямую :)
        Glide.with(this)
            .load(trackInfo.coverArtwork)
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .into(trackImage)

        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val albumInfo = findViewById<TextView>(R.id.album)
        val albumIngoText = trackInfo.nameOfTheBand
        if (albumIngoText.isNotEmpty()) {
            albumGroup.isVisible = true
            albumInfo.text = albumIngoText
        }
        findViewById<TextView>(R.id.genre).text = trackInfo.genre
        findViewById<TextView>(R.id.country).text = trackInfo.country
        findViewById<TextView>(R.id.year).text = trackInfo.releaseYear
        findViewById<TextView>(R.id.trackName).text = trackInfo.trackName
        findViewById<TextView>(R.id.nameOfTheBand).text = trackInfo.nameOfTheBand
        findViewById<TextView>(R.id.duration).text = trackInfo.duration
    }

    private fun audioPlayerOnPause() {
        uiHandler.removeCallbacks(updatePlayProgressTask)
        playButton.setImageResource(R.drawable.ic_button_play)
    }

    override fun onPause() {
        audioInteractor.pausePlayer { audioPlayerOnPause() }
        super.onPause()
    }

    override fun onDestroy() {
        uiHandler.removeCallbacks(updatePlayProgressTask)
        audioInteractor.releasePlayer()
        super.onDestroy()
    }
}