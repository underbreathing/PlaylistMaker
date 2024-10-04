package com.example.playlistmaker.search.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackRvItemBinding
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Track) {
        binding.trackArtist.text = model.artistName
        binding.trackName.text = model.trackName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        Glide.with(itemView)
            .load(model.artworkUrl)
            .placeholder(R.drawable.placeholder_track)
            .transform(MultiTransformation(CenterCrop(),RoundedCorners(2)))
            .into(binding.trackImage)
    }
}