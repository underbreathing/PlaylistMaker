package com.example.playlistmaker.main.ui.activity

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.media_library.ui.MediaLibraryActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.mainButtonSearch.setOnClickListener {
            val intentMedia = Intent(this,SearchActivity::class.java)
            startActivity(intentMedia)
        }

        binding.mainButtonMedia.setOnClickListener {
            val intentMedia = Intent(this,MediaLibraryActivity::class.java)
            startActivity(intentMedia)
        }


        binding.mainButtonSettings.setOnClickListener {
            val intentMedia = Intent(this,SettingsActivity::class.java)
            startActivity(intentMedia)
        }

    }
}