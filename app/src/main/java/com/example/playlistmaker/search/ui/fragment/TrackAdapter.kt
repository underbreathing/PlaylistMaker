package com.example.playlistmaker.search.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackRvItemBinding
import com.example.playlistmaker.search.domain.model.Track

class TrackAdapter(
    private val tracks: List<Track>, private val onTrackClickListener: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(TrackRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = tracks.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        if (onTrackClickListener != null) {//такая вот заглушка
            holder.itemView.setOnClickListener { onTrackClickListener.invoke(tracks[position]) }
        }
    }
}