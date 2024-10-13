package com.example.playlistmaker.media_library.ui.child.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRvItemBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo

class PlaylistAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {

    private val playlists: MutableList<PlaylistInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            PlaylistRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun setNewPlaylists(playlists: List<PlaylistInfo>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
    }
}