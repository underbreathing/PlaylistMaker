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
import com.example.playlistmaker.edit_playlist.ui.FragmentEditPlaylist
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.media_player.ui.fragment.FragmentMediaPlayer
import com.example.playlistmaker.playlist.ui.rv.PlaylistTracksAdapter
import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.playlist.ui.view_model.model.PlaylistScreenData
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentPlaylist : Fragment() {

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

        viewModel?.initData()

        var generalInfo: PlaylistUi? = null
        var playlistTracks: List<Track> = emptyList()
        var isGeneralInfoWasDisplayed = false
        viewModel?.playlistData?.observe(viewLifecycleOwner) { playlistData ->
            when (playlistData) {
                is PlaylistScreenData.GeneralInfo -> {
                    generalInfo = playlistData.generalInfo
                    if (!isGeneralInfoWasDisplayed) {
                        playlistData.generalInfo?.let { showGeneralInfo(it) }
                    }
                    isGeneralInfoWasDisplayed = true
                }

                is PlaylistScreenData.Tracks -> {
                    updateTracks(
                        playlistData.totalTimeMinutes,
                        playlistData.tracksCount
                    )
                    playlistTracks = playlistData.tracks
                    updateRV(playlistTracks)
                }
            }
        }

        binding.tbButtonTopBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rvPlaylistTracks.adapter = adapter

        binding.bShare.setOnClickListener {
            generalInfo?.let {
                sharePlaylist(it, playlistTracks)
            }
        }

        val bottomSheetSettings = BottomSheetBehavior.from(binding.bsSettings).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.bsSettings.setOnClickListener {}
        binding.xmlPlaylist.lPlaylistRoot.isEnabled = false

        bottomSheetSettings.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlayStateChange(false)
                    }

                    else -> {
                        overlayStateChange(true)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.tvPlaylistTitle.post {
            bottomSheetSettings.peekHeight =
                binding.root.height - binding.tvPlaylistTitle.y.toInt() - binding.tvPlaylistTitle.height - 4
        }

        binding.bMenu.setOnClickListener {
            bottomSheetSettings.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.tvShare.setOnClickListener {
            generalInfo?.let {
                sharePlaylist(it, playlistTracks)
            }
        }

        binding.tvDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlist_delete_dialog_title))
                .setMessage(
                    getString(
                        R.string.playlist_delete_dialog_message,
                        generalInfo?.title.orEmpty()
                    )
                )
                .setPositiveButton(getString(R.string.material_dialog_positive)) { _, _ ->
                    viewModel?.deletePlaylist(playlistTracks)

                }
                .setNegativeButton(getString(R.string.material_dialog_negative)) { _, _ -> }
                .show()
        }

        viewModel?.playlistDeleteState?.observe(viewLifecycleOwner) { isPlaylistDeleted ->
            if (isPlaylistDeleted) {
                findNavController().navigateUp()
            }
        }

        binding.tvEditInf.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPlaylist_to_fragmentEditPlaylist,
                generalInfo?.let { FragmentEditPlaylist.createArgs(it) })
        }
        binding.overlay.setOnClickListener {
            bottomSheetSettings.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun playlistToString(generalInfo: PlaylistUi, tracks: List<Track>): String {
        return "${generalInfo.title} ${if (generalInfo.description.isNotBlank()) "\n${generalInfo.description}" else ""} " +
                "\n${
                    getString(
                        R.string.playlist_track_count,
                        generalInfo.trackCount.toString(),
                        resources.getQuantityString(
                            R.plurals.track,
                            generalInfo.trackCount
                        )
                    )
                }" +

                tracks.mapIndexed { index, track ->
                    "\n${index + 1}. ${track.artistName} - ${track.trackName} (${
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)
                    }) "
                }.joinToString()
    }

    private fun overlayStateChange(isVisible: Boolean) {
        binding.overlay.isVisible = isVisible
        binding.overlay.isClickable = isVisible
    }

    private fun sharePlaylist(generalInfo: PlaylistUi, playlistTracks: List<Track>) {
        if (adapter.itemCount == 0) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_share_tracks_empty_message),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, playlistToString(generalInfo, playlistTracks))
            startActivity(
                Intent.createChooser(
                    shareIntent, getString(R.string.playlist_share_title)
                )
            )
        }
    }

    private fun updateRV(tracks: List<Track>) {
        adapter.setItems(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showGeneralInfo(generalInfo: PlaylistUi) {
        binding.tvPlaylistTitle.text = generalInfo.title
        val description = generalInfo.description
        if (description.isNotEmpty()) {
            binding.tvPlaylistDesc.isVisible = true
            binding.tvPlaylistDesc.text = description
        }
        Glide.with(binding.root).load(generalInfo.coverUriString?.let(Uri::parse))
            .error(R.drawable.placeholder_track).placeholder(R.drawable.placeholder_track)
            .into(binding.ivPlaylistCover)

        binding.root.post {
            BottomSheetBehavior.from(binding.bsTrackFrame).peekHeight =
                binding.root.height - binding.clPlaylistInfoContainer.height - 16
        }
        Glide.with(binding.bsSettings)
            .load(generalInfo.coverUriString?.let(Uri::parse))
            .error(R.drawable.placeholder_track)
            .placeholder(R.drawable.placeholder_track)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(8)))
            .into(binding.xmlPlaylist.ivCover)
        binding.xmlPlaylist.tvTitle.text = generalInfo.title
    }

    private fun updateTracks(totalTimeMinutes: String, tracksCount: Int) {
        val tracksCountString = getString(
            R.string.playlist_track_count,
            tracksCount.toString(),
            resources.getQuantityString(R.plurals.track, tracksCount)
        )
        binding.tvTracksCount.text = tracksCountString
        binding.xmlPlaylist.tvTrackCount.text = tracksCountString

        binding.tvMinutes.text = getString(
            R.string.playlist_tracks_total_time,
            totalTimeMinutes,
            resources.getQuantityString(
                R.plurals.minutes,
                if (totalTimeMinutes.isNotEmpty()) totalTimeMinutes.toInt() else 0
            )
        )
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "playlist data key"
        fun createArgs(playlist: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to playlist)
        }
    }
}