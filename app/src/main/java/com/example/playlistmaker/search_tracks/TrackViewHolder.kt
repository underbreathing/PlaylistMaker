package com.example.playlistmaker.search_tracks

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val artistTextView: TextView
    private val nameTextView: TextView
    private val timeTextView: TextView
    private val artworkImageView: ImageView

    init {
        artistTextView = parentView.findViewById(R.id.track_artist)
        nameTextView = parentView.findViewById(R.id.track_name)
        timeTextView = parentView.findViewById(R.id.track_time)
        artworkImageView = parentView.findViewById(R.id.track_image)
    }

    fun bind(model: Track) {
        artistTextView.text = model.artistName
        nameTextView.text = model.trackName
        timeTextView.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl)
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkImageView)
    }
}