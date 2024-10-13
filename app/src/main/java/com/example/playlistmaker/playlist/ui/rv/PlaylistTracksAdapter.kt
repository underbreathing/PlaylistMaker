package com.example.playlistmaker.playlist.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackRvItemBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.fragment.TrackViewHolder

class PlaylistTracksAdapter(
    private val onClick: (Track) -> Unit,
    private val onLongClick: (Track) -> Boolean
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private val items: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            TrackRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
        holder.itemView.setOnLongClickListener {
            onLongClick(currentItem)
        }
        holder.itemView.setOnClickListener {
            onClick(currentItem)
        }
    }

    fun setItems(newItems: List<Track>) {
        items.clear()
        items.addAll(newItems)
    }
}