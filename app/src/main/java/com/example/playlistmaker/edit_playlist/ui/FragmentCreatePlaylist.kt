package com.example.playlistmaker.edit_playlist.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class FragmentCreatePlaylist : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    protected val binding get() = _binding!!

    private var shouldShowDialog = false

    protected open val viewModel: CreatePlaylistViewModel by viewModel()
    private var savedUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
                val navBarsInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                if (imeInsets.bottom == 0) {
                    v.setPadding(0, 0, 0, navBarsInsets.bottom)
                } else {
                    v.setPadding(0, 0, 0, imeInsets.bottom)
                }
                insets
            }
        } else {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        return binding.root
    }


    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val activity = requireActivity()
            activity.window.setSoftInputMode(
                activity.packageManager.getActivityInfo(
                    activity.componentName, PackageManager.GET_META_DATA
                ).softInputMode
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSavingCompleted.observe(viewLifecycleOwner) { isSavingOver ->
            if (isSavingOver) {
                findNavController().navigateUp()
            }
        }

        binding.tbButtonTopBack.setOnClickListener {
            navigateUp()
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            navigateUp()
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
                    Glide.with(requireContext()).load(uri).transform(
                        MultiTransformation(
                            CenterCrop(), RoundedCorners(
                                PLAYLIST_FRAME_CORNER_RADIUS
                            )
                        )
                    ).into(binding.ivAddPlaylistCover)
                    shouldShowDialog = true
                } else {
                    Log.d("MYYY", "image not selected")
                }
            }

        binding.ivAddPlaylistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.bCreate.setOnClickListener {
            savePlaylist(savedUri)
        }
    }

    protected open fun navigateUp() {
        if (shouldShowDialog) {
            showExitDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    protected open fun savePlaylist(savedUri: Uri?) {
        val playlistName: String = binding.etTitle.text.toString()
        viewModel.savePlaylist(
            playlistName, binding.etDescription.text.toString().ifEmpty { null }, savedUri
        )
        context?.let {
            Toast.makeText(
                it,
                getString(R.string.add_playlist_toast_message, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showExitDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it).setTitle(getString(R.string.add_playlist_dialog_title))
                .setMessage(getString(R.string.add_playlist_dialog_description))
                .setNeutralButton(getString(R.string.add_playlist_button_cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.add_playlist_button_exit)) { _, _ ->
                    findNavController().navigateUp()
                }.show()
        }
    }

    private fun onEditTextDescriptionChange(descriptionIsEmpty: Boolean) {
        shouldShowDialog = !descriptionIsEmpty
        binding.etDescription.setBackgroundResource(
            if (descriptionIsEmpty) R.drawable.add_playlist_et_empty
            else R.drawable.add_playlist_et_not_empty
        )
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

    companion object{
        const val PLAYLIST_FRAME_CORNER_RADIUS = 8
    }
}