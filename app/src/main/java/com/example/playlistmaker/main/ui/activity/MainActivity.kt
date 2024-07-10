package com.example.playlistmaker.main.ui.activity

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.ui.view_model.MainViewModel
import com.example.playlistmaker.media_library.ui.MediaLibraryActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, MainViewModel.getMainViewModelFactory(this)
        )[MainViewModel::class.java]

        binding.mainButtonSearch.setOnClickListener {
            viewModel.goToActivity(SearchActivity::class.java)
        }

        binding.mainButtonMedia.setOnClickListener {
            viewModel.goToActivity(MediaLibraryActivity::class.java)
        }


        binding.mainButtonSettings.setOnClickListener {
            viewModel.goToActivity(SettingsActivity::class.java)
        }

    }
}