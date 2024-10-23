package com.example.playlistmaker.playlist.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media_player.ui.fragment.FragmentMediaPlayer
import com.example.playlistmaker.playlist.ui.model.PlaylistCompleteDataUi
import com.example.playlistmaker.playlist.ui.rv.PlaylistTracksAdapter
import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class FragmentPlaylist : Fragment() {

    companion object {
        private const val PLAYLIST_ID_KEY = "playlist data key"
        fun createArgs(playlist: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to playlist)
        }
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var viewModel: PlaylistViewModel? = null

    private val adapter = PlaylistTracksAdapter({ track ->
        findNavController().navigate(
            R.id.action_fragmentPlaylist_to_fragmentMediaPlayer,
            FragmentMediaPlayer.createArgs(track)
        )
    }, { track ->
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.material_dialog_playlist_title))
            .setPositiveButton(getString(R.string.material_dialog_positive)) { _, _ ->
                viewModel?.deleteTrack(track)
            }.setNegativeButton(getString(R.string.material_dialog_negative)) { _, _ -> }.show()
        true
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel {
            parametersOf(arguments?.getLong(PLAYLIST_ID_KEY))
        }

        var playlistLastV: PlaylistCompleteDataUi? = null

        viewModel?.receivedPlaylist?.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                updateAndShowData(it)
                updateRV(playlist.tracks)
                playlistLastV = playlist
            }
        }

        binding.tbButtonTopBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rvPlaylistTracks.adapter = adapter

        binding.bShare.setOnClickListener {
            sharePlaylist(playlistLastV)
        }

        val bottomSheetSettings = BottomSheetBehavior.from(binding.bsSettings).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        val bottomSheetTracks = BottomSheetBehavior.from(binding.bsTrackFrame)
        binding.bsSettings.setOnClickListener{}
        binding.xmlPlaylist.lPlaylistRoot.isEnabled = false

        bottomSheetSettings.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlayStateChange(false)
                        bottomSheetTracks.isDraggable = true
                    }

                    else -> {
                        overlayStateChange(true)
                        bottomSheetTracks.isDraggable = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.tvPlaylistTitle.post {
            bottomSheetSettings.peekHeight = binding.root.height - binding.tvPlaylistTitle.y.toInt() - binding.tvPlaylistTitle.height - 4
        }

        binding.bMenu.setOnClickListener {
            bottomSheetSettings.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.tvShare.setOnClickListener {
            sharePlaylist(playlistLastV)
        }


    }

    private fun overlayStateChange(isVisible: Boolean) {
        binding.overlay.isVisible = isVisible
        binding.overlay.isClickable = isVisible
    }

    private fun sharePlaylist(playlistLastV: PlaylistCompleteDataUi?) {
        if (adapter.itemCount == 0) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val toto = playlistLastV?.toString(requireContext())
            println(toto)
            shareIntent.putExtra(Intent.EXTRA_TEXT, playlistLastV?.toString(requireContext()))
            startActivity(
                Intent.createChooser(
                    shareIntent, "Отправить плейлист"
                )
            )
        }
    }

    private fun updateRV(tracks: List<Track>) {
        adapter.setItems(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun updateAndShowData(completeData: PlaylistCompleteDataUi) {
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

        binding.root.post {
            BottomSheetBehavior.from(binding.bsTrackFrame).peekHeight =
                binding.root.height - binding.clPlaylistInfoContainer.height - 16
        }

        binding.xmlPlaylist.tvTitle.text = completeData.generalInfo?.title
        Glide.with(binding.bsSettings)
            .load(completeData.generalInfo?.coverUriString?.let(Uri::parse))
            .error(R.drawable.placeholder_track)
            .placeholder(R.drawable.placeholder_track)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(8)))
            .into(binding.xmlPlaylist.ivCover)
        val trackCount = completeData.generalInfo?.trackCount ?: 0
        binding.xmlPlaylist.tvTrackCount.text = getString(
            R.string.playlist_track_count,
            trackCount.toString(),
            resources.getQuantityString(R.plurals.track, trackCount)
        )

    }

}