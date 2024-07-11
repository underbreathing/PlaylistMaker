package com.example.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.search.domain.model.Track

class TrackAdapter(
    private val tracks: List<Track>, private val onTrackClickListener: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(TrackBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = tracks.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        if (onTrackClickListener != null) {//такая вот заглушка
            holder.itemView.setOnClickListener { onTrackClickListener.invoke(tracks[position]) }
        }
    }
}