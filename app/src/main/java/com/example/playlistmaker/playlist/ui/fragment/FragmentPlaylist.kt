package com.example.playlistmaker.playlist.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media_player.ui.fragment.FragmentMediaPlayer
import com.example.playlistmaker.playlist.ui.PlaylistCompleteDataUi
import com.example.playlistmaker.playlist.ui.rv.PlaylistTracksAdapter
import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class FragmentPlaylist : Fragment() {

    companion object {
        private const val PLAYLIST_ID_KEY = "playlist data key"
        fun createArgs(playlist: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to playlist)
        }
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by inject()

    private val adapter = PlaylistTracksAdapter({ track ->
        findNavController().navigate(
            R.id.action_fragmentPlaylist_to_fragmentMediaPlayer,
            FragmentMediaPlayer.createArgs(track)
        )
    }, {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Хотите удалить трек?")
            .setPositiveButton("Да") { _, _ ->
                //TODO удалить трек
            }.setNegativeButton("Нет") { _, _ -> }.show()
        true
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        arguments?.getLong(PLAYLIST_ID_KEY)?.let {
            viewModel.initData(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.receivedPlaylist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                showData(it)
                updateRV(playlist.tracks)
            }
        }

        binding.tbButtonTopBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.clPlaylistInfoContainer.viewTreeObserver.addOnGlobalLayoutListener {

        }

        binding.root.post {
            BottomSheetBehavior.from(binding.bsTrackFrame).peekHeight =
                binding.root.height - binding.clPlaylistInfoContainer.height - 16
        }

        binding.rvPlaylistTracks.adapter = adapter
    }

    private fun updateRV(tracks: List<Track>) {
        adapter.setItems(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showData(completeData: PlaylistCompleteDataUi) {
        val generalInfo = completeData.generalInfo
        binding.tvPlaylistTitle.text = generalInfo?.title
        val description = generalInfo?.description
        if (!description.isNullOrEmpty()) {
            binding.tvPlaylistDesc.isVisible = true
            binding.tvPlaylistDesc.text = description
        }
        Glide.with(binding.root).load(generalInfo?.coverUriString?.let(Uri::parse))
            .error(R.drawable.placeholder_track).placeholder(R.drawable.placeholder_track)
            .into(binding.ivPlaylistCover)
        binding.tvTracksCount.text = getString(
            R.string.playlist_track_count,
            generalInfo?.trackCount.toString(),
            resources.getQuantityString(R.plurals.track, generalInfo?.trackCount ?: 0)
        )


        binding.tvMinutes.text = getString(
            R.string.playlist_tracks_total_time,
            completeData.totalTimeMinutes,
            resources.getQuantityString(
                R.plurals.minutes,
                if (completeData.totalTimeMinutes.isNotEmpty()) completeData.totalTimeMinutes.toInt() else 0
            )
        )
    }

}