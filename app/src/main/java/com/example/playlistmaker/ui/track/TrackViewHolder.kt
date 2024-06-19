package com.example.playlistmaker.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parentView.context).inflate(R.layout.track, parentView, false)) {
    private val artistTextView: TextView = itemView.findViewById(R.id.track_artist)
    private val nameTextView: TextView = itemView.findViewById(R.id.track_name)
    private val timeTextView: TextView = itemView.findViewById(R.id.track_time)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        artistTextView.text = model.artistName
        nameTextView.text = model.trackName
        timeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        Glide.with(itemView)
            .load(model.artworkUrl)
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkImageView)
    }
}