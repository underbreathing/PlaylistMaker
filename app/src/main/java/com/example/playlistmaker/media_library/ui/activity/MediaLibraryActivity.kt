package com.example.playlistmaker.media_library.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.media_library.ui.MediaLibraryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTopBack.setOnClickListener {
            finish()
        }

        binding.mediaViewPager.adapter =
            MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator =
            TabLayoutMediator(binding.mediaTabs, binding.mediaViewPager) { tabItem, position ->
                when (position) {
                    0 -> tabItem.text = getString(R.string.tab_item1)
                    1 -> tabItem.text = getString(R.string.tab_item2)
                }
            }
        tabMediator.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}