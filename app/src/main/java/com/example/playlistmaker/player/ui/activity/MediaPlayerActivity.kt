package com.example.playlistmaker.player.ui.activity


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.player.ui.mapper.TimeFormatter
import com.example.playlistmaker.player.ui.mapper.TrackMapper
import com.example.playlistmaker.player.ui.model.TrackInfo
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

const val TRACK_INTENT_DATA = "track_intent_data"

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaPlayerBinding
    private lateinit var viewModel: MediaPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedTrack = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_INTENT_DATA, Track::class.java)
        } else {
            intent.getParcelableExtra(TRACK_INTENT_DATA)
        })
        receivedTrack?.let {
            showTrackInfo(TrackMapper.map(it))
        }

        viewModel = getViewModel { parametersOf(receivedTrack?.previewUrl) }


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
            trackCurrentTime.text = TimeFormatter.formatTheTime(it.progress.toLong())
        }

        playButton.setOnClickListener {
            viewModel.playToggle()
        }
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    private fun showTrackInfo(trackInfo: TrackInfo) {
        Glide.with(this).load(trackInfo.coverArtwork).placeholder(R.drawable.placeholder_track)
            .centerCrop().into(binding.trackArtwork)

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
}