package com.example.playlistmaker.create_playlist.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

const val PLAYLIST_CORNER_RADIUS = 8

class FragmentCreatePlaylist : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var shouldShowDialog = false

    private val viewModel: CreatePlaylistViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var savedUri: Uri? = null

        viewModel.isSavingCompleted.observe(viewLifecycleOwner) { isSavingOver ->
            if (isSavingOver) {
                findNavController().navigateUp()
            }
        }

        binding.tbButtonTopBack.setOnClickListener {
            if (shouldShowDialog) {
                showExitDialog()
            } else {
                findNavController().navigateUp()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner){
            if (shouldShowDialog) {
                showExitDialog()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.etTitle.addTextChangedListener(onTextChanged = { charSequence, _, _, _ ->
            charSequence?.isEmpty()?.let { onEditTextTitleChange(it) }
        })

        binding.etDescription.addTextChangedListener(onTextChanged = { charSequence, _, _, _ ->
            charSequence?.isEmpty()?.let { onEditTextDescriptionChange(it) }
        })

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    savedUri = uri
                    Glide.with(requireContext())
                        .load(uri)
                        .transform(
                            MultiTransformation(
                                CenterCrop(), RoundedCorners(
                                    PLAYLIST_CORNER_RADIUS
                                )
                            )
                        )
                        .into(binding.ivAddPlaylistCover)
                    shouldShowDialog = true
                } else {
                    Log.d("MYYY", "image not selected")
                }
            }

        binding.ivAddPlaylistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.bCreate.setOnClickListener {
            val playlistName: String = binding.etTitle.text.toString()
            viewModel.savePlaylist(
                playlistName,
                binding.etDescription.text.toString().ifEmpty { null },
                savedUri
            )
            context?.let {
                Toast.makeText(
                    it,
                    getString(R.string.add_playlist_toast_message, playlistName),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showExitDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.add_playlist_dialog_title))
                .setMessage(getString(R.string.add_playlist_dialog_description))
                .setNeutralButton(getString(R.string.add_playlist_button_cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.add_playlist_button_exit)) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    private fun onEditTextDescriptionChange(descriptionIsEmpty: Boolean) {
        shouldShowDialog = !descriptionIsEmpty
        if (descriptionIsEmpty) {
            binding.etDescription.setBackgroundResource(R.drawable.add_playlist_et_empty)
        } else {
            binding.etDescription.setBackgroundResource(R.drawable.add_playlist_et_not_empty)
        }
        binding.tvOnEtDescription.isVisible = !descriptionIsEmpty
    }

    private fun onEditTextTitleChange(titleIsEmpty: Boolean) {
        binding.bCreate.isEnabled = !titleIsEmpty
        shouldShowDialog = !titleIsEmpty
        if (titleIsEmpty) {
            binding.etTitle.setBackgroundResource(R.drawable.add_playlist_et_empty)
        } else {
            binding.etTitle.setBackgroundResource(R.drawable.add_playlist_et_not_empty)
        }
        binding.tvOnEtTitle.isVisible = !titleIsEmpty
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}