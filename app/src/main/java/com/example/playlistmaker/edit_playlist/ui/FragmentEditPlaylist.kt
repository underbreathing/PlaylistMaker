package com.example.playlistmaker.edit_playlist.ui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media_library.ui.model.PlaylistUi
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentEditPlaylist : FragmentCreatePlaylist() {

    companion object {
        private const val PLAYLIST_INFO_KEY = "playlist info key"

        fun createArgs(playlistInfo: PlaylistUi): Bundle {
            return bundleOf(PLAYLIST_INFO_KEY to playlistInfo)
        }
    }

    override val viewModel: EditPlaylistViewModel by viewModel()
    private var receivedPlaylistUi: PlaylistUi? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receivedPlaylistUi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST_INFO_KEY, PlaylistUi::class.java)
        } else {
            arguments?.getParcelable(PLAYLIST_INFO_KEY)
        }

        binding.tbButtonTopBack.title = getString(R.string.edit_playlist_title)
        binding.bCreate.text = getString(R.string.edit_playlist_button_text)

        receivedPlaylistUi?.let { playlistUi ->
            binding.etTitle.setText(playlistUi.title)
            binding.etDescription.setText(playlistUi.description)
            //val playlistCover = if(playlistUi.coverUriString == null) R.drawable.placeholder_track else Uri.parse(playlistUi.coverUriString)
            Glide.with(binding.root)
                .load(Uri.parse(playlistUi.coverUriString))
                .error(R.drawable.placeholder_track)
                .placeholder(R.drawable.placeholder_track)
                .transform(
                    MultiTransformation(
                        CenterCrop(), RoundedCorners(
                            PLAYLIST_FRAME_CORNER_RADIUS
                        )
                    )
                )
                .into(binding.ivAddPlaylistCover)
        }
    }

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun savePlaylist(savedUri: Uri?) {
        val coverUriString = receivedPlaylistUi?.coverUriString
        viewModel.updatePlaylist(
            receivedPlaylistUi,
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString(),
            if (savedUri == null && coverUriString != null) Uri.parse(coverUriString) else savedUri
        )
    }
}