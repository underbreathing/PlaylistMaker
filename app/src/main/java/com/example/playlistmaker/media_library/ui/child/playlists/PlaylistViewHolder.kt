package com.example.playlistmaker.media_library.ui.child.playlists

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.playlistmaker.databinding.PlaylistRvItemBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo

class PlaylistViewHolder(private val binding: PlaylistRvItemBinding) : ViewHolder(binding.root) {

    fun bind(playlist: PlaylistInfo) {
        binding.ivCover.setImageURI(Uri.parse(playlist.coverUriString))
        binding.tvTitle.text = playlist.title
        binding.tvTrackCount.text = playlist.trackCount
    }
}