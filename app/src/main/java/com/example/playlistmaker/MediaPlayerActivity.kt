package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.search_tracks.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val trackData = intent.getParcelableExtra<Track>(TRACK_INTENT_DATA)

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


    }
}