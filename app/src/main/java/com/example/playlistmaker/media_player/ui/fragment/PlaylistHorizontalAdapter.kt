package com.example.playlistmaker.media_player.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRvItemHorizontalBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfoUi

class PlaylistHorizontalAdapter(private val onClick: ((playlist: PlaylistInfoUi) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistHorizontalViewHolder>() {

    private val playlists: MutableList<PlaylistInfoUi> = mutableListOf()

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

    fun setNewItems(newPlaylists: List<PlaylistInfoUi>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
    }
}