package com.example.playlistmaker.media_player.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaPlayerBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistInfoUi
import com.example.playlistmaker.media_library.ui.view_model.state.PlaylistsDataState
import com.example.playlistmaker.media_player.ui.animations.PlayerAnimations
import com.example.playlistmaker.media_player.ui.mapper.TimeFormatter
import com.example.playlistmaker.media_player.ui.mapper.TrackMapper
import com.example.playlistmaker.media_player.ui.model.TrackInfo
import com.example.playlistmaker.media_player.ui.view_model.MediaPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class FragmentMediaPlayer : Fragment() {


    companion object {
        const val TRACK_INTENT_DATA = "track_intent_data"
        fun createArgs(track: Track): Bundle = bundleOf(
            TRACK_INTENT_DATA to track
        )
    }

    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding: FragmentMediaPlayerBinding get() = _binding!!
    private var viewModel: MediaPlayerViewModel? = null
    private val trackMapper by inject<TrackMapper>()
    private var isTrackInMediaLibrary: Boolean = false
    private val playerAnimations by inject<PlayerAnimations>()
    private val playlistAdapter = PlaylistHorizontalAdapter {
        viewModel?.addTrackToPlaylist(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MYYY", "onViewCreated")

        val currentTrack: Track = requireNotNull(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(TRACK_INTENT_DATA, Track::class.java)
            } else {
                arguments?.getParcelable(TRACK_INTENT_DATA)
            }
        )

        showTrackInfo(trackMapper.map(currentTrack))

        viewModel = getViewModel { parametersOf(currentTrack.previewUrl, currentTrack) }

        viewModel?.onCreateView()

        viewModel?.observeTrackInMediaLibrary()?.observe(viewLifecycleOwner) { isInMediaLibrary ->
            isTrackInMediaLibrary = isInMediaLibrary
            if (isInMediaLibrary) {
                binding.likeInner.setImageResource(R.drawable.ic_inner_like_checked)
            } else {
                binding.likeInner.setImageResource(R.drawable.ic_inner_like)
            }
            playerAnimations.animateInnerLikeIn(binding.likeInner)
        }

        binding.buttonTopBack.setOnClickListener {
            findNavController().navigateUp()
        }
        val playButton = binding.buttonPlay
        playButton.isEnabled = false
        val trackCurrentTime = binding.trackCurrentTime


        viewModel?.getMediaPlayerStateLiveData()?.observe(viewLifecycleOwner) {
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA") when (it) {
                MediaPlayerViewModel.MediaPlayerState.READY -> {
                    playButton.isEnabled = true
                }

                MediaPlayerViewModel.MediaPlayerState.COMPLETED -> {
                    trackCurrentTime.text = getString(R.string.media_player_play_progress_start)
                    playButton.setImageResource(R.drawable.ic_button_play)
                }
            }
        }

        viewModel?.getPlayStatusLiveData()?.observe(viewLifecycleOwner) {
            if (it.isPlaying) {
                playButton.setImageResource(R.drawable.ic_button_pause)
            } else {
                playButton.setImageResource(R.drawable.ic_button_play)
            }
            trackCurrentTime.text = TimeFormatter.formatTheTime(it.progress.toLong())
        }

        playButton.setOnClickListener {
            viewModel?.playToggle()
        }

        binding.buttonLike.setOnClickListener {
            playerAnimations.animateInnerLikeOut(binding.likeInner)
            if (isTrackInMediaLibrary) {
                viewModel?.deleteFromMediaLibrary(currentTrack.trackId)
            } else {
                viewModel?.saveTrackToMediaLibrary(
                    currentTrack, System.currentTimeMillis()
                )
            }

        }

        binding.rvAddToPlaylist.adapter = playlistAdapter

        viewModel?.observePlaylistLiveData()?.observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsDataState.Content -> showPlaylists(it.data)
                PlaylistsDataState.Empty -> showEmpty()
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bsAddToPlaylist).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlayStateChange(false)
                        setEnableAllButtons(true)
                    }

                    else -> {
                        overlayStateChange(true)
                        setEnableAllButtons(false)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.buttonAddToMedia.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel?.observeAddTrackToPlaylistState()?.observe(viewLifecycleOwner) { addedState ->
            if (addedState.isAdded) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            showToast(
                if (addedState.isAdded) getString(
                    R.string.media_player_toast_new_track_added, addedState.playlistTitle
                )
                else getString(R.string.media_player_toast_old_track, addedState.playlistTitle)
            )
        }

        binding.bNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_fragmentMediaPlayer_to_fragmentCreatePlaylist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.onDestroy()
    }

    private fun showToast(toastMessage: String) {
        context?.let {
            Toast.makeText(
                it, toastMessage, Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun showEmpty() {
        binding.rvAddToPlaylist.isVisible = false
    }

    private fun showPlaylists(data: List<PlaylistInfoUi>) {
        binding.rvAddToPlaylist.isVisible = true
        playlistAdapter.setNewItems(data)
        playlistAdapter.notifyDataSetChanged()
    }

    private fun setEnableAllButtons(isEnabled: Boolean) {
        binding.buttonLike.isEnabled = isEnabled
        binding.buttonPlay.isEnabled = isEnabled
        binding.buttonAddToMedia.isEnabled = isEnabled
    }

    private fun overlayStateChange(isVisible: Boolean) {
        binding.overlay.isVisible = isVisible
        binding.overlay.isClickable = isVisible
    }

    override fun onPause() {
        viewModel?.pausePlayer()
        super.onPause()
    }

    private fun showTrackInfo(trackInfo: TrackInfo) {
        Glide.with(this).load(trackInfo.coverArtwork).placeholder(R.drawable.placeholder_track)
            .transform(CenterCrop(), RoundedCorners(8)).into(binding.trackArtwork)

        val albumIngoText = trackInfo.nameOfTheBand
        if (albumIngoText.isNotEmpty()) {
            binding.albumGroup.isVisible = true
            binding.album.text = albumIngoText
        }
        binding.genre.text = trackInfo.genre
        binding.country.text = trackInfo.country
        val releaseYear = trackInfo.releaseYear
        if (releaseYear.isNotEmpty()) {
            binding.releaseDateGroup.isVisible = true
            binding.year.text = releaseYear
        }
        binding.playerTrackName.text = trackInfo.trackName
        binding.nameOfTheBand.text = trackInfo.nameOfTheBand
        binding.duration.text = trackInfo.duration
    }

}