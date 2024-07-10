package com.example.playlistmaker.player.ui.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.player.ui.mapper.TimeFormatter
import com.example.playlistmaker.player.ui.mapper.TrackMapper
import com.example.playlistmaker.player.ui.model.TrackInfo
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel

const val TRACK_INTENT_DATA = "track_intent_data"

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaPlayerBinding
    private lateinit var viewModel: MediaPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, MediaPlayerViewModel.getMediaPlayerViewModelFactory(this)
        )[MediaPlayerViewModel::class.java]

        viewModel.getArrivedTrack()?.let {
            viewModel.preparePlayer(it.previewUrl)
            showTrackInfo(TrackMapper.map(it))
        }

        binding.buttonTopBack.setOnClickListener { finish() }
        val playButton = binding.buttonPlay
        playButton.isEnabled = false
        val trackCurrentTime = binding.trackCurrentTime


        viewModel.getMediaPlayerStateLiveData().observe(this) {
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA") when (it) {
                MediaPlayerViewModel.MediaPlayerState.READY -> {
                    playButton.isEnabled = true
                }

                MediaPlayerViewModel.MediaPlayerState.COMPLETED -> {
                    Log.d("MYY", "COMPLETED PLAY")

                    trackCurrentTime.text = getString(R.string.mediaplayer_play_progress_start)
                    playButton.setImageResource(R.drawable.ic_button_play)
                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) {
            if (it.isPlaying) {
                playButton.setImageResource(R.drawable.ic_button_pause)
                Log.d("MYY", "PAUSE")
            } else {
                playButton.setImageResource(R.drawable.ic_button_play)
                Log.d("MYY", "PLAY")
            }
            trackCurrentTime.text =
                TimeFormatter.formatTheTime(it.progress.toLong()) // может оно только когда isPlaying?
        }

        playButton.setOnClickListener {
            viewModel.playToggle()
        }
    }

    private fun showTrackInfo(trackInfo: TrackInfo) {

        val trackImage = findViewById<ImageView>(R.id.track_artwork)

        Glide.with(this).load(trackInfo.coverArtwork).placeholder(R.drawable.placeholder_track)
            .centerCrop().into(trackImage)

        val albumIngoText = trackInfo.nameOfTheBand
        if (albumIngoText.isNotEmpty()) {
            binding.albumGroup.isVisible = true
            binding.album.text = albumIngoText
        }
        binding.genre.text = trackInfo.genre
        binding.country.text = trackInfo.country
        binding.year.text = trackInfo.releaseYear
        binding.playerTrackName.text = trackInfo.trackName
        binding.nameOfTheBand.text = trackInfo.nameOfTheBand
        binding.duration.text = trackInfo.duration
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }
}