package com.example.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.example.playlistmaker.media_library.ui.view_model.SelectedTracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SelectedTracksFragment: Fragment() {

    companion object{
        fun getInstance(): SelectedTracksFragment = SelectedTracksFragment()
    }


    private lateinit var binding: FragmentSelectedTracksBinding
    private val viewModel: SelectedTracksFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectedTracksBinding.inflate(inflater,container,false)
        return binding.root
    }


}