package com.example.playlistmaker.player.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRvItemHorizontalBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfo

class PlaylistHorizontalAdapter(private val onClick: ((playlist: PlaylistInfo) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistHorizontalViewHolder>() {

    val playlists: MutableList<PlaylistInfo> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistHorizontalViewHolder {
        return PlaylistHorizontalViewHolder(
            PlaylistRvItemHorizontalBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistHorizontalViewHolder, position: Int) {
        val currentPlaylist = playlists[position]
        holder.bind(currentPlaylist)
        if (onClick != null) {
            holder.itemView.setOnClickListener { onClick.invoke(currentPlaylist) }
        }
    }
}