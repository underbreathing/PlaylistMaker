package com.example.playlistmaker.media_library.ui.child.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRvItemBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistUi

class PlaylistAdapter(private val onClick: (PlaylistUi) -> Unit) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    private val playlists: MutableList<PlaylistUi> = mutableListOf()

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
        val current = playlists[position]
        holder.bind(current)
        holder.itemView.setOnClickListener { onClick(current) }
    }

    fun setNewPlaylists(playlists: List<PlaylistUi>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
    }
}