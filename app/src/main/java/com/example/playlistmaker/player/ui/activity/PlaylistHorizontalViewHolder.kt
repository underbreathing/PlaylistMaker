package com.example.playlistmaker.player.ui.activity

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistRvItemHorizontalBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo
import com.example.playlistmaker.utils.Utils

class PlaylistHorizontalViewHolder(private val binding: PlaylistRvItemHorizontalBinding): ViewHolder(binding.root) {

    fun bind(playlist: PlaylistInfo) {
        val path: Any =
            if (playlist.coverUriString != null) Uri.parse(playlist.coverUriString) else R.drawable.placeholder_track
        Glide.with(itemView)
            .load(path)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(8)))
            .into(binding.ivCover)

        binding.tvTitle.text = playlist.title
        binding.tvTrackCount.text = Utils.getTrackDeclension(playlist.trackCount)
    }
}