package com.example.playlistmaker.media_player.ui.fragment

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistRvItemHorizontalBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistUi

class PlaylistHorizontalViewHolder(private val binding: PlaylistRvItemHorizontalBinding) :
    ViewHolder(binding.root) {

    fun bind(playlist: PlaylistUi) {
        Glide.with(itemView)
            .load(playlist.coverUriString?.let(Uri::parse))
            .error(R.drawable.placeholder_track)
            .placeholder(R.drawable.placeholder_track)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(8)))
            .into(binding.ivCover)

        binding.tvTitle.text = playlist.title
        binding.tvTrackCount.text =
            itemView.context.getString(
                R.string.playlist_track_count,
                playlist.trackCount.toString(),
                itemView.context.resources.getQuantityString(R.plurals.track, playlist.trackCount)
            )
    }
}