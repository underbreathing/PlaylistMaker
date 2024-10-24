package com.example.playlistmaker.media_library.ui.child.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.media_library.ui.view_model.state.PlaylistsDataState
import com.example.playlistmaker.playlist.ui.fragment.FragmentPlaylist
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentPlaylists : Fragment() {
    companion object {
        fun getInstance(): FragmentPlaylists = FragmentPlaylists()
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() {
            return _binding!!
        }

    private val viewModel: PlaylistsFragmentViewModel by viewModel()

    private var adapter: PlaylistAdapter = PlaylistAdapter { playlistInfo ->
        findNavController().navigate(
            R.id.action_fragmentMediaLibrary_to_fragmentPlaylist,
            FragmentPlaylist.createArgs(playlistInfo.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDataState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsDataState.Content -> showContent(it.data)
                PlaylistsDataState.Empty -> showEmpty()
            }
        }

        binding.rvPlaylists.adapter = adapter

        binding.bNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMediaLibrary_to_fragmentCreatePlaylist)

        }

    }

    private fun showEmpty() {
        binding.rvPlaylists.isVisible = false
        setPlaceholderVisibility(true)
    }

    private fun setPlaceholderVisibility(isVisible: Boolean) {
        binding.problemAdditionalMessage.isVisible = isVisible
        binding.mediaSelectedTracksPlaceholder.isVisible = isVisible
    }

    private fun showContent(content: List<PlaylistUi>) {
        setPlaceholderVisibility(false)
        binding.rvPlaylists.isVisible = true
        adapter.setNewPlaylists(content)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}