package com.example.playlistmaker.media_library.ui.child.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRvItemBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfoUi

class PlaylistAdapter(private val onClick: (PlaylistInfoUi) -> Unit) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    private val playlists: MutableList<PlaylistInfoUi> = mutableListOf()

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

    fun setNewPlaylists(playlists: List<PlaylistInfoUi>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
    }
}