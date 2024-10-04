package com.example.playlistmaker.media_library.ui.child.favorite_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.media_library.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.media_library.ui.view_model.state.MediaLibraryDataState
import com.example.playlistmaker.player.ui.activity.FragmentMediaPlayer
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.fragment.TrackAdapter
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.GlobalScope
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment() {

    companion object {
        fun getInstance(): FavoriteTracksFragment = FavoriteTracksFragment()
    }


    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding: FragmentFavoriteTracksBinding get() = _binding!!

    private val viewModel: FavoriteTracksViewModel by viewModel()

    private val tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter? = null
    private var isClickAllowed: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackAdapter(tracks) {
            trackDebounce(it)
        }

        viewModel.observeMediaLibraryData().observe(viewLifecycleOwner) {
            when (it) {
                is MediaLibraryDataState.Content -> showContent(it.tracks)
                is MediaLibraryDataState.Empty -> showEmpty(it.message)
            }
        }

        binding.rvMediaLibrary.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMediaLibrary.adapter = trackAdapter
    }


    private fun trackDebounce(track: Track) {
        if (isClickAllowed) {
            isClickAllowed = false
            debounce<Boolean>(
                false, GlobalScope,
                TrackAdapter.CLICK_DEBOUNCE_DELAY
            ) { value ->
                isClickAllowed = value
            }.invoke(true)
            findNavController().navigate(
                R.id.action_fragmentMediaLibrary_to_fragmentMediaPlayer,
                FragmentMediaPlayer.createArgs(track)
            )
        }
    }

    private fun showEmpty(message: String) {
        binding.rvMediaLibrary.isVisible = false
        setPlaceholdersVisibility(true)
        binding.tvPlaceholderAdditionalMessage.text = message
    }

    private fun setPlaceholdersVisibility(visibility: Boolean) {
        binding.ivPlaceholder.isVisible = visibility
        binding.tvPlaceholderAdditionalMessage.isVisible = visibility
    }

    private fun showContent(tracks: List<Track>) {
        setPlaceholdersVisibility(false)
        binding.rvMediaLibrary.isVisible = true
        this.tracks.clear()
        this.tracks.addAll(tracks)
        trackAdapter?.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        trackAdapter = null
    }

}